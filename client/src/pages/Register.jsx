import "bootstrap/dist/css/bootstrap.min.css";
import {Alert, Button, Col, Container, Form, InputGroup, Row} from "react-bootstrap";
import {useState} from "react";
import logo from "../logo.svg";
import {Navigate, useNavigate} from "react-router-dom";
import {useAuth} from "../hooks/auth";
import API from "../api/api";

function Register() {
    const [validated, setValidated] = useState(false);
    const [registerError, setRegisterError] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const {profile} = useAuth();
    const navigate = useNavigate();

    if (!!profile) {
        return <Navigate to="/home"></Navigate>;
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        event.stopPropagation();
        const form = event.currentTarget;
        if (form.checkValidity() === false) {
            setValidated(true)
            return;
        }
        const username = event.target.email.value;
        const password = event.target.password.value;
        const fullName = event.target.fullName.value;
        const age = event.target.age.valueAsNumber;
        console.log(username, password, fullName, age, event.target)
        if(!username || !password || !fullName || !age) {
            console.log("Missing fields");
            setRegisterError(true);
            setValidated(true)
            return;
        }

        const successful = await API.Security.register(username, password, fullName, age);
        if (successful) {
            setRegisterError(false);
            console.log("Registered successfully")
            navigate("/login")
        } else {
            console.log("Could not register");
            setRegisterError(true);
        }
        setValidated(true);
    };

    return (
        <>
            <Container>
                <Row className={"my-2"}>
                    <img src={logo} height={"80"} width={"100%"} alt={"Ticket34 Logo"}/>
                </Row>
                <Form noValidate className={""} validated={validated} onSubmit={handleSubmit}>
                    <Form.Group className="mb-3" controlId="formBasicEmail">
                        <Form.Label>Email Address</Form.Label>
                        <Form.Control
                            required={true}
                            type="email"
                            placeholder="Enter email"
                            name="email"
                        />
                        <Form.Control.Feedback type="invalid">
                            Please provide a valid email.
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Label htmlFor={"password"}>Password</Form.Label>
                    <InputGroup className="" controlId="formBasicPassword">
                        <Form.Control
                            required={true}
                            type={showPassword ? "text" : "password"}
                            placeholder="Password"
                            name="password"
                            id="password"
                            pattern={"^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"}
                            aria-describedby="passwordHelpBlock"
                        />
                        <Button className={"show"} onClick={() => setShowPassword((prev) => !prev)}>
                            {showPassword ? "Hide" : "Show"}
                        </Button>
                        <Form.Control.Feedback type="invalid">
                            Please provide a valid password.
                        </Form.Control.Feedback>
                    </InputGroup>
                    <Form.Text id="passwordHelpBlock" muted>
                        Your password must be at least 8 characters long, contain capital and lowecase letters and numbers,
                        and must not contain spaces, special characters, or emoji.
                    </Form.Text>
                    <Form.Group className="my-3" controlId="formBasicName">
                        <Form.Label>Full Name</Form.Label>
                        <Form.Control
                            required={true}
                            type="text"
                            placeholder="Full Name"
                            name="fullName"
                            minLength={3}
                            pattern={"[a-zA-Z]+ +[a-zA-Z]+"}
                        />
                        <Form.Control.Feedback type="invalid">
                            Please provide a valid name.
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="formBasicAge">
                        <Form.Label>Age</Form.Label>
                        <Form.Control
                            required={true}
                            type="number"
                            placeholder="Age"
                            name="age"
                            min={18}
                            max={120}
                            step={1}
                        />
                        <Form.Control.Feedback type="invalid">
                            Please provide a valid age.
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Col className="d-flex gap-2">
                        <Button variant="primary" type="submit">
                            Sign Up
                        </Button>
                    </Col>
                </Form>
                {registerError && (
                    <Alert key={"danger"} variant={"danger"} dismissible className="my-2">
                        Bad Credentials!
                    </Alert>
                )}
            </Container>
        </>
    );
}

export default Register;
