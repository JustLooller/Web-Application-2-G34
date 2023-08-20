import 'bootstrap/dist/css/bootstrap.min.css';
import {Container, Button, Form, Row, Col, Alert} from "react-bootstrap";
import {useContext, useState} from "react";
import logo from '../logo.svg'
import axios from "axios";
import {Navigate} from "react-router-dom";
import {Authentication} from "../App";
import {setItem} from "localforage";


function Login() {

    const [validated, setValidated] = useState(false);
    const [loginError, setLoginError] = useState(false);
    const [succesfulLogin, setSuccesfulLogin] = useState(false);

    //test useContext
    //const {authentication, setAuthentication} = useContext(Authentication);
    const authentication = JSON.parse(localStorage.getItem("authentication"))

    const handleSubmit = async (event) => {
        console.log("submitted")
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }

        // POST request using fetch with async/await
        const requestOptions = {
            mode: 'cors',
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({username: 'customeruser', password: 'customer'})
        };
        try {
            event.preventDefault();
            event.stopPropagation();
            /*const response = fetch('http://localhost:8081/api/login', requestOptions);
            const data = await response;
            console.log(data.valu.json())

            */
            const response = await axios.post('http://localhost:8081/api/login', {
                username: event.target.email.value,
                password: event.target.password.value
            })
            localStorage.setItem("authentication", JSON.stringify(response.data))
            //setAuthentication(response.data)
            setSuccesfulLogin(true)

        } catch (e) {
            console.log(e)
            setLoginError(true)
        }

        setValidated(true);
    };

    if(succesfulLogin || authentication!=null)
        return <Navigate to="/warranty"></Navigate>

    return (
        <>
            <Container>
                <Row className={"my-2"}>
                    <img src={logo} height={"80"} width={"100%"}/>
                </Row>
                <Form noValidate validated={validated} onSubmit={handleSubmit}>
                    <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label>Email address</Form.Label>
                        <Form.Control /*required*/ type="text" placeholder="Enter email" name="email"/>
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formBasicPassword">
                        <Form.Label>Password</Form.Label>
                        <Form.Control /*required*/ type="password" placeholder="Password" name="password"/>
                    </Form.Group>
                    <Col className="d-flex gap-2">
                        <Button variant="primary" type="submit">
                            Sign In
                        </Button>
                        <a href={"/signup"}>
                            <Button variant="outline-secondary" type="button">
                                Sign Up
                            </Button>
                        </a>
                    </Col>
                </Form>
                {loginError &&
                    <Alert key={'danger'} variant={'danger'} className="my-2">
                        Bad Credentials!
                    </Alert>
                }
            </Container>
        </>
    );
}

export default Login;