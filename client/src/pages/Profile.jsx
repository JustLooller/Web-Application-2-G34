import { useAuth } from "../hooks/auth";
import NavigationBar from "./NavigationBar";
import React, { useState } from "react";
import { Container, Form, Button, Row, Col } from "react-bootstrap";

export default function Profile() {
  const { profile } = useAuth();
  const [newEmail, setNewEmail] = useState("");
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmNewPassword, setConfirmNewPassword] = useState("");

  const handleEmailChange = (e) => {
    setNewEmail(e.target.value);
  };

  const handleOldPasswordChange = (e) => {
    setOldPassword(e.target.value);
  };
  const handleNewPasswordChange = (e) => {
    setNewPassword(e.target.value);
  };
  const handleConfirmNewPasswordChange = (e) => {
    setConfirmNewPassword(e.target.value);
  };
  const handleChangeEmailConfirmation = (e) => {
    e.preventDefault();
    console.log("Email changed to: " + newEmail);
  };
  const handleChangePasswordConfirmation = (e) => {
    e.preventDefault();
    console.log("Old password: " + oldPassword);
    console.log("New password: " + newPassword);
    console.log("Confirm new password: " + confirmNewPassword);
  };
  

  return (
    <>
      <NavigationBar />
      <Container>
        <Row>
          <h1 className="text-center">{profile.name}'s profile</h1>
          <h5 className="text-center">Role: {profile.role}</h5>
          <h5 className="text-start mt-2">Email: {profile.email}</h5>
        </Row>
        <Row>
          <Col>
            <Form onSubmit={handleChangeEmailConfirmation}>
              <Form.Group controlId="changeEmail">
                <Form.Label>Change Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="New Email"
                  onChange={handleEmailChange}
                />
                <Button variant="primary" type="submit" className="mt-2">
                  Save
                </Button>
              </Form.Group>
            </Form>
          </Col>
          <Col>
            <Form onSubmit={handleChangePasswordConfirmation}>
              <Form.Group controlId="changePassword">
                <Form.Label>Change Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Old Password"
                  onChange={handleOldPasswordChange}
                />
                <Form.Control
                  type="password"
                  placeholder="New Password"
                  onChange={handleNewPasswordChange}
                  className="mt-2"
                />
                <Form.Control
                  type="password"
                  placeholder="Confirm New Password"
                  onChange={handleConfirmNewPasswordChange}
                  className="mt-2"
                />

                <Button variant="primary" type="submit" className="mt-2">
                  Save
                </Button>
              </Form.Group>
            </Form>
          </Col>
        </Row>
      </Container>
    </>
  );
}
