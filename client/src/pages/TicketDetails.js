//import 'bootstrap/dist/css/bootstrap.min.css';
import {
  Button,
  Col,
  Container,
  Form,
  Row,
  Spinner,
  Table,
} from "react-bootstrap";
import { useEffect, useRef, useState } from "react";
import avatarCustomer from "../avatarCustomer.svg";
import avatarExpert from "../avatarExpert.svg";
import { useAuth } from "../hooks/auth";
import { Message, Role, Ticket } from "../models";
import API from "../api/api";
import { useParams } from "react-router-dom";
import NavigationBar from "./NavigationBar";
import { Client } from "@stomp/stompjs";
import notificationSound from "./../iphone_14_notification.mp3";

function TicketDetails() {
  const [messageSending, setMessageSending] = useState(false);
  const [messages, setMessages] = useState([]);

  //test useContext
  const { profile } = useAuth();

  const { id } = useParams();
  const [ticketDetails, setTicketDetails] = useState(null);

  let socketActivated = false;
  let audio = new Audio(notificationSound);

  const messagesEndRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const handleSubmit = (event) => {
    event.preventDefault();
    event.stopPropagation();
    setMessageSending((old) => true);
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      console.log("invalid");
      return;
    }

    const message = new Message(form.newMessage.value, profile.email, id, null);
    const file = form.attachment.files[0];
    API.MessageAPI.sendMessage(message, file)
      .then((res) => {
        form.newMessage.value = "";
        form.attachment.value = null;
      })
      .catch((e) => console.log(e))
      .finally(() => {
        setMessageSending((old) => false);
      });
  };

  const handleDownloadImage = async (ticket_id, attachment) => {
    try {
      const response = API.MessageAPI.getAttachment(ticket_id, attachment);
      const fileURL = URL.createObjectURL(await response);
      window.open(fileURL);
    } catch (error) {
      console.error(error);
    }
  };

  const notificationWebSocket = () => {
    const stompClient = new Client({
      brokerURL: "ws://localhost:8081/ws",
    });

    stompClient.onConnect = (frame) => {
      console.log("Connected: " + frame);
      let subscription = stompClient.subscribe(
        "/chat/" + id.toString(),
        (greeting) => {
          console.log("Subscribed: " + greeting.body);
          const newMessage = Message.fromJson(greeting.body);
          if (newMessage.user_mail !== profile.email) {
            audio.play();
          }
          setMessages((old) => [...old, newMessage]);
        }
      );
    };
    stompClient.activate();
  };

  useEffect(() => {
    if (!socketActivated) {
      socketActivated = true;
      notificationWebSocket();
    }

    API.TicketAPI.getTicketById(id)
      .then((res) => setTicketDetails(Ticket.fromJson(res)))
      .catch((e) => console.log(e));
    API.MessageAPI.getMessages(id)
      .then((res) => setMessages(res))
      .catch((e) => console.log(e));
  }, []);

  return (
    <>
      <NavigationBar />
      <Container
        className="mt-3"
        style={{ height: "calc(100vh - 39vh)", overflowY: "auto" }}
      >
        {ticketDetails !== null && (
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>#</th>
                <th>Status</th>
                <th>Expert</th>
                <th>Warranty</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{ticketDetails.id}</td>
                <td>{ticketDetails.state.replace("_"," ")}</td>
                <td>
                  {ticketDetails.expert_mail === null
                    ? "NOT ASSIGNED"
                    : ticketDetails.expert_mail}
                </td>
                <td>{ticketDetails.sale_id}</td>
              </tr>
            </tbody>
          </Table>
        )}
        {messages.map((m, index) => (
          <ChatMessage
            key={m.text + index}
            text={m.text}
            myMessage={
              (profile.role === Role.CUSTOMER && m.user_mail === ticketDetails.creator_email) ||
              (profile.role === Role.MANAGER && m.user_mail === ticketDetails.creator_email) ||
              (profile.role === Role.EXPERT && m.user_mail !== ticketDetails.creator_email)
            }
            message={m}
            ticket_id={ticketDetails.id}
            download_function={handleDownloadImage}
            isCustomer = {m.user_mail === ticketDetails.creator_email}
          ></ChatMessage>
        ))}

        <div
          style={{ float: "left", clear: "both" }}
          ref={messagesEndRef}
        ></div>
      </Container>
      {profile.role !== Role.MANAGER && (
        <Container className={"mt-1"}>
          <Form className="d-flex" onSubmit={handleSubmit}>
            <Col>
              <Form.Group className="mb-3" controlId="formBasicEmail">
                <Form.Control
                  type="text"
                  placeholder="New Message"
                  name={"newMessage"}
                  required
                />
                <Form.Control type="file" name={"attachment"} />
              </Form.Group>
            </Col>
            <Col className="mx-2" sm="auto">
              <Button variant="primary" type="submit">
                {messageSending && (
                  <Spinner animation="grow" size="sm"></Spinner>
                )}
                {!messageSending && <>Send</>}
              </Button>
            </Col>
          </Form>
        </Container>
      )}
    </>
  );
}

export default TicketDetails;

function ChatMessage(props) {
  return (
    <>
      <Row
        className="py-3"
        style={{ flexDirection: props.myMessage ? "row" : "row-reverse" }}
      >
        <Col
          style={{
            backgroundColor: props.myMessage ? "#105662" : "#6AAEBA",
            color: "white",
            padding: "10px",
            marginLeft: props.myMessage ? "70px" : "0px",
            marginRight: props.myMessage ? "0px" : "70px",
            borderRadius: "10px",
          }}
        >
          {props.text}
        </Col>
        <Col sm="auto" className="d-flex align-items-center">
          <img src={props.isCustomer ? avatarCustomer : avatarExpert} height="50px" width="50px" alt={"AvatarCustomer"} />
        </Col>
        
          <Row className={"mx-0 mt-1"} style={{ flexDirection: "row" , textAlign: props.myMessage ? "end" : "start"}}>
            <h6 className={"text-muted" }>{props.message.user_mail}</h6>
          </Row>
        
      </Row>

      {props.message.attachment != null && (
        <Col
          style={{
            marginLeft: props.myMessage ? "60px" : "0px",
            marginRight: props.myMessage ? "0px" : "60px",
          }}
        >
          Attachment:
          <Button
            style={{
              marginLeft: "10px",
            }}
            onClick={() =>
              props.download_function(props.ticket_id, props.message.attachment)
            }
          >
            {Message.fileName(props.message.attachment)}
          </Button>
        </Col>
      )}
    </>
  );
}
