import 'bootstrap/dist/css/bootstrap.min.css';
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {useEffect, useState} from "react";
import logo from '../logo.svg'
import avatar from '../avatar.svg'
import {useAuth} from "../hooks/auth";
import {Role} from "../models";
import API from "../api/api";


function OpenTicket() {

    const [validated, setValidated] = useState(false);
    const [messages, setMessages] = useState([]);

    //test useContext
    const {profile} = useAuth();
    const handleSubmit = (event) => {
        event.preventDefault();
        event.stopPropagation();
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            console.log("invalid")
            return;
        }
        const newMessage = event.target.newMessage.value
        setMessages((oldMessages) => ([...oldMessages, {text: newMessage, expert: profile.role === Role.EXPERT}]))
        setValidated(true);
    };

    useEffect(() => {

        const getProducts = async () => {
            const response = await API.ProductsAPI.getAll();
            console.log(response);
            setMessages(response.map((m) => {
                return {text: m.description, expert: false}
            }))
        }
        getProducts();
    }, []);

    return (
        <>
            <Row className={"my-2"}>
                <img src={logo} height={"80"} width={"100%"}/>
            </Row>
            <Container>
                <Row className="d-flex justify-content-between">
                    <Col className="d-flex align-items-center">
                        TICKET <b>#12345</b>
                    </Col>
                    <Col className="d-flex align-items-center">
                        STATUS: <b>OPEN</b>
                    </Col>
                    <Col className="d-flex align-items-center">
                        EXPERT: <b>Pippo Franco</b>
                    </Col>
                </Row>
                {messages.map((m) => (
                    <ChatMessage key={m.text} text={m.text} expert={m.expert}></ChatMessage>
                ))}
                <Form className='d-flex fixed-bottom mx-2' onSubmit={handleSubmit}>
                    <Col>
                        <Form.Group className="mb-3" controlId="formBasicEmail">
                            <Form.Control type="text" placeholder="New Message" name={"newMessage"}/>
                        </Form.Group>
                    </Col>
                    <Col className="mx-2" sm="auto">
                        <Button variant="primary" type="submit">
                            Send
                        </Button>
                    </Col>
                </Form>
            </Container>

        </>
    );
}

export default OpenTicket;

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