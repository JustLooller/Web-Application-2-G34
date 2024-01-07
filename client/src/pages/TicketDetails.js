//import 'bootstrap/dist/css/bootstrap.min.css';
import {Button, Col, Container, Form, Row, Spinner, Table} from "react-bootstrap";
import {useEffect, useRef, useState} from "react";
import avatar from '../avatar.svg'
import {useAuth} from "../hooks/auth";
import {Message, Ticket} from "../models";
import API from "../api/api";
import {Link, useParams} from "react-router-dom";
import NavigationBar from "./NavigationBar";
import {Client} from '@stomp/stompjs'
import notificationSound from './../iphone_14_notification.mp3';

const RETRIEVE_MSG_INTERVAL = 1000 * 10; // 10 seconds

function TicketDetails() {

    const [messageSending, setMessageSending] = useState(false)
    const [messages, setMessages] = useState([]);

    //test useContext
    const {profile} = useAuth();

    const {id} = useParams();
    const [ticketDetails, setTicketDetails] = useState(null);


    let socketActivated = false;
    let audio = new Audio(notificationSound)

    const bottomDiv = useRef(null);

    const handleSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        setMessageSending((old) => true)
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            console.log("invalid")
            return;
        }

        const message = new Message(form.newMessage.value, profile.email, id, null)
        const file = form.attachment.files[0]
        API.MessageAPI.sendMessage(message, file)
            .then(res => {
                form.newMessage.value = "";
                form.attachment.value = null;
            })
            .catch(e => console.log(e))
            .finally(() => {
                setMessageSending((old) => false)
            })
    };

    const handleDownloadImage = async (ticket_id, attachment) => {
        try {
            const response = API.MessageAPI.getAttachment(ticket_id, attachment)
            const fileURL = URL.createObjectURL(await response);
            window.open(fileURL);
        } catch (error) {
            console.error(error);
        }
    };

    const notificationWebSocket = () => {
        const stompClient = new Client({
            brokerURL: 'ws://localhost:8081/ws'
        });

        stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);
            let subscription = stompClient.subscribe('/chat/' + id.toString(), (greeting) => {
                console.log("Subscribed: " + greeting.body)
                const newMessage = Message.fromJson(greeting.body)
                if (newMessage.user_mail !== profile.email) {
                    audio.play()
                }
                setMessages((old) => [...old, newMessage]);
                scrollToTheBottom()
            });
        };
        stompClient.activate()
    }

    const scrollToTheBottom = () => {
        setTimeout(() => {
            console.log("I'm scrolling to the bottom!");
            bottomDiv.current.scrollIntoView({behavior: 'smooth'});
        }, 1000)
    };


    useEffect(() => {

        if (!socketActivated) {
            socketActivated = true
            notificationWebSocket()
        }


        API.TicketAPI.getTicketById(id)
            .then((res) => setTicketDetails(Ticket.fromJson(res)))
            .catch((e) => console.log(e))
        API.MessageAPI.getMessages(id)
            .then(res => setMessages(res))
            .catch((e) => console.log(e))
    }, []);

    return (<>
        <NavigationBar/>
        <Container className="mt-3" style={{height: "calc(100vh - 39vh)", overflowY: "auto"}}>
            {ticketDetails !== null && <Table striped bordered hover>
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
                    <td>{ticketDetails.state}</td>
                    <td>{(ticketDetails.expert_mail === null) ? "NOT ASSIGNED" : ticketDetails.expert_mail}</td>
                    <td>{ticketDetails.sale_id}</td>
                </tr>
                </tbody>
            </Table>}
            {messages.map((m, index) => (
                <ChatMessage key={m.text + index} text={m.text} expert={m.user_mail !== profile.email} message={m}
                             ticket_id={ticketDetails.id} download_function={handleDownloadImage}></ChatMessage>))}

            <div ref={bottomDiv}></div>
        </Container>
        <Container className={'mt-1'}>
            <Form className='d-flex' onSubmit={handleSubmit}>
                <Col>
                    <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Control type="text" placeholder="New Message" name={"newMessage"} required/>
                        <Form.Control type="file" name={"attachment"}/>
                    </Form.Group>
                </Col>
                <Col className="mx-2" sm="auto">
                    <Button variant="primary" type="submit">
                        {messageSending && <Spinner animation="grow" size="sm"></Spinner>}
                        {!messageSending && <>Send</>}

                    </Button>
                </Col>
            </Form>
        </Container>

    </>);
}

export default TicketDetails;

function ChatMessage(props) {
    return (<>
        <Row className="py-3" style={{flexDirection: (props.expert) ? "row-reverse" : "row"}}>
            <Col style={{
                backgroundColor: (props.expert) ? "#6AAEBA" : "#105662",
                color: "white",
                padding: "10px",
                borderRadius: "10px"
            }}>
                {props.text}
            </Col>
            <Col sm="auto" className="d-flex align-items-center mx-3">
                <img src={avatar} height="50px" width="50px" alt={"Avatar"}/>
            </Col>
        </Row>
        {(props.message.attachment != null) && <Button
            onClick={() => props.download_function(props.ticket_id, props.message.attachment)}>{Message.fileName(props.message.attachment)}</Button>}
    </>);
}