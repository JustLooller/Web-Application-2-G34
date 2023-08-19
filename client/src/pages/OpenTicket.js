import 'bootstrap/dist/css/bootstrap.min.css';
import {Container, Button, Form, Row, Col, Navbar, Table} from "react-bootstrap";
import {useContext, useEffect, useState} from "react";
import logo from '../logo.svg'
import avatar from '../avatar.svg'
import send from '../send.svg'
import {Authentication} from "../App";
import axios from "axios";

function OpenTicket() {

    const [validated, setValidated] = useState(false);
    const [messages, setMessages] = useState([]);

    //test useContext
    //const {authentication, setAuthentication} = useContext(Authentication);
    const authentication = JSON.parse(localStorage.getItem("authentication"))

    const handleSubmit = (event) => {
        console.log(authentication.access_token)
        const form = event.currentTarget;
        event.preventDefault();
        const newMessage = event.target.newMessage.value
        setMessages([...messages, {text:newMessage, expert:false}])
        event.target.newMessage.value = '';
        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }

        setValidated(true);
    };

    useEffect(() => {
        console.log("useEffect called")
        const config = {
            headers: {Authorization: `Bearer ${authentication.access_token}`}
        };
        axios.get("http://localhost:8081/api/products/", config).then(
            (response)=>setMessages(
                response.data.map((m)=>{return {text:m.description, expert:false}})
            )
        )

    }, []);

    return (
        <>
            <Row className={"my-2"}>
                <img src={logo} height={"80"} width={"100%"}/>
            </Row>
            <Container>
                <Row className="d-flex justify-content-between">
                    <Col class="d-flex align-items-center">
                        TICKET <b>#12345</b>
                    </Col>
                    <Col class="d-flex align-items-center">
                        STATUS: <b>OPEN</b>
                    </Col>
                    <Col class="d-flex align-items-center">
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
                    <img src={avatar} height="50px" width="50px"/>
                </Col>
            </Row>
        </>
    );
}