//import 'bootstrap/dist/css/bootstrap.min.css';
import {Button, Col, Container, Form, Row, Spinner, Table} from "react-bootstrap";
import {useEffect, useState} from "react";
import logo from '../logo.svg'
import avatar from '../avatar.svg'
import {useAuth} from "../hooks/auth";
import {Message, Role, Ticket} from "../models";
import API from "../api/api";
import {useParams} from "react-router-dom";
import NavigationBar from "./NavigationBar";


function TicketDetails() {

    const [messageSending, setMessageSending] = useState(false)
    const [messages, setMessages] = useState([]);

    //test useContext
    const {profile} = useAuth();

    const {id} = useParams();
    const [ticketDetails, setTicketDetails] = useState(null);

    const handleSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        setMessageSending(true)
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            console.log("invalid")
            return;
        }

        const message = new Message(form.newMessage.value, profile.email, id)
        API.MessageAPI.sendMessage(message)
            .then(res => {
                setMessages((old)=>[...old,message]);
                form.newMessage.value = "";
            })
            .catch(e=>console.log(e))
        setMessageSending(false)
    };

    useEffect(() => {
        API.TicketAPI.getTicketById(id)
            .then((res)=>setTicketDetails(Ticket.fromJson(res)))
            .catch(
                (e)=>console.log(e)
            )

        API.MessageAPI.getMessages(id)
            .then(res=>setMessages(res))
            .catch((e)=>console.log(e))
    }, []);

    return (
        <>
            <NavigationBar/>
            <Container className="mt-3">
                {ticketDetails !== null &&
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
                            <td>{ticketDetails.state}</td>
                            <td>{(ticketDetails.expert_mail===null)? "NOT ASSIGNED" : ticketDetails.expert_mail}</td>
                            <td>{ticketDetails.sale_id}</td>
                        </tr>
                        </tbody>
                    </Table>
                }
                {messages.map((m) => (
                    <ChatMessage key={m.text} text={m.text} expert={m.user_mail !== profile.email}></ChatMessage>
                ))}

                <Form className='d-flex fixed-bottom mx-2' onSubmit={handleSubmit}>
                    <Col>
                        <Form.Group className="mb-3" controlId="formBasicEmail">
                            <Form.Control type="text" placeholder="New Message" name={"newMessage"} required/>
                        </Form.Group>
                    </Col>
                    <Col className="mx-2" sm="auto">
                        <Button variant="primary" type="submit">
                            {
                                messageSending &&
                                <Spinner animation="grow" size="sm"></Spinner>
                            }
                            {
                                !messageSending &&
                                <>Send</>
                            }

                        </Button>
                    </Col>
                </Form>
            </Container>

        </>
    );
}

export default TicketDetails;

function ChatMessage(props) {
    return (
        <>
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
        </>
    );
}