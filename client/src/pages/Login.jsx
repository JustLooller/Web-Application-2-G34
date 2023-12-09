import "bootstrap/dist/css/bootstrap.min.css";
import { Alert, Button, Col, Container, Form, Row } from "react-bootstrap";
import { useState } from "react";
import logo from "../logo.svg";
import { Link, Navigate } from "react-router-dom";
import { useAuth } from "../hooks/auth";
import LoadingSpinner from "../components/LoadingSpinner/LoadingSpinner";

function Login() {
  const [isLoggingIn, setIsLoggingIn] = useState(false);
  const [validated, setValidated] = useState(false);
  const [loginError, setLoginError] = useState(false);
  const [succesfulLogin, setSuccesfulLogin] = useState(false);

  const { profile, login } = useAuth();

  if (profile) {
    return <Navigate to="/home"></Navigate>;
  }

  const handleSubmit = async (event) => {
    event.preventDefault();
    event.stopPropagation();
    const form = event.currentTarget;
    if (form.checkValidity() === false) {
      setLoginError(true)
      return;
    }
    const username = event.target.email.value;
    const password = event.target.password.value;
    setIsLoggingIn(true);
    setLoginError(false);
    const hasLoggedIn = await login(username, password);
    if (hasLoggedIn) {
      setLoginError(false);
      setSuccesfulLogin(true);
    } else {
      console.log("Could not login");
      setLoginError(true);
    }
    setValidated(true);
    setIsLoggingIn(false);
  };

  if (succesfulLogin) return <Navigate to="/home"></Navigate>;

  return (
    <>
      <Container>
        <Row className={"my-2"}>
          <img src={logo} height={"80"} width={"100%"} alt={"Ticket34 Logo"} />
        </Row>
        <Row className={"text-center"}>
          <h1>Sign In</h1>
        </Row>
        <Form noValidate validated={validated} onSubmit={handleSubmit}>
          <Form.Group className="mb-3" controlId="formBasicEmail">
            <Form.Label>Email Address</Form.Label>
            <Form.Control
              required={true}
              type="email"
              placeholder="mario@reds.it"
              name="email"
            />
            <Form.Control.Feedback type="invalid">
              Please provide a valid email.
            </Form.Control.Feedback>
          </Form.Group>
          <Form.Group className="mb-3" controlId="formBasicPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              required={true}
              type="password"
              placeholder="********"
              name="password"
            />
            <Form.Control.Feedback type="invalid">
              Please provide a valid password.
            </Form.Control.Feedback>
          </Form.Group>
          <Col className="d-flex gap-2">
            <Button variant="primary" type="submit">
              Sign In
            </Button>
            <Link to={"/signup"}>
              <Button variant="outline-secondary" type="button">
                Sign Up
              </Button>
            </Link>
          </Col>
        </Form>
        {isLoggingIn && (
          <Container className={"mt-2"}>
            <LoadingSpinner></LoadingSpinner>
          </Container>
        )}
        {loginError && (
          <Alert key={"danger"} variant={"danger"} dismissible className="my-2">
            Bad Credentials!
          </Alert>
        )}
      </Container>
    </>
  );
}

export default Login;
