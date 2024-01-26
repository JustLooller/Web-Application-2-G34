import { useAuth } from "../hooks/auth";
import { useNavigate } from "react-router-dom";
import NavigationBar from "./NavigationBar";
import React, { useEffect, useState } from "react";
import {
  Container,
  Form,
  Button,
  Row,
  Col,
  Alert,
  InputGroup,
} from "react-bootstrap";
import API from "../api/api";

export default function Profile() {
  const { profile, logout } = useAuth();
  const navigator = useNavigate();
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmNewPassword, setConfirmNewPassword] = useState("");
  const [changePasswordStatus, setChangePasswordStatus] = useState({});

  const handleOldPasswordChange = (e) => {
    setOldPassword(e.target.value);
  };
  const handleNewPasswordChange = (e) => {
    setNewPassword(e.target.value);
  };
  const handleConfirmNewPasswordChange = (e) => {
    setConfirmNewPassword(e.target.value);
  };
  const handleChangePasswordConfirmation = (e) => {
    e.preventDefault();
    if (changePasswordStatus.type === "success") return;
    if (oldPassword === "" || newPassword === "" || confirmNewPassword === "") {
      setChangePasswordStatus({
        type: "danger",
        message: "Please fill in all fields",
      });
      return;
    }
    if (newPassword !== confirmNewPassword) {
      setChangePasswordStatus({
        type: "danger",
        message: "New password and confirm new password do not match",
      });
      return;
    }
    API.ProfileAPI.changePassword(
      profile.email,
      oldPassword,
      newPassword,
      confirmNewPassword
    )
      .then((res) => {
        if (res.status === 200) {
          setChangePasswordStatus({
            type: "success",
            message:
              "Password changed successfully - redirecting to login",
          });
        } else {
          setChangePasswordStatus({ type: "danger", message: res.detail });
        }
      })
      .catch((err) => {
        setChangePasswordStatus({
          type: "danger",
          message: err.response.data.detail,
        });
      });
  };
  useEffect(() => {
    if (changePasswordStatus.type === "success") {
      // start a 3 second timer, logout and redirect to login
      setTimeout(() => {
        logout();
        navigator("/login");
      }, 3000);
    }
  }, [changePasswordStatus]);

  return (
    <>
      <NavigationBar />
      <Container>
        <Row>
          <h1 className="text-center mt-2">{profile.name}'s profile</h1>
          <h5 className="text-center mt-2">Role: {profile.role}</h5>
          <h5 className="text-center mt-2">Email: {profile.email}</h5>
        </Row>
        <Row style={{width: "75%" , margin: "2rem auto",}}>
          <Col>
            <Form onSubmit={handleChangePasswordConfirmation}>
              <Form.Group controlId="changePassword">
                <Form.Label>Change Password</Form.Label>
                  <Form.Control
                    required={true}
                    placeholder="Old Password"
                    name="password"
                    id="password"
                    aria-describedby="passwordHelpBlock"
                    onChange={handleOldPasswordChange}
                  />
                <Form.Control
                  type="password"
                  placeholder="New Password"
                  onChange={handleNewPasswordChange}
                  pattern={
                    "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"
                  }
                  className="mt-2"
                />
                <Form.Control
                  type="password"
                  placeholder="Confirm New Password"
                  onChange={handleConfirmNewPasswordChange}
                  pattern={
                    "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$"
                  }
                  className="mt-2"
                />
                <Form.Text id="passwordHelpBlock" muted>
                  Your password must be at least 8 characters long, contain
                  capital and lowecase letters and numbers, and must not contain
                  spaces, special characters, or emoji.
                </Form.Text>
                <br></br>
                <Button variant="primary" type="submit" className="mt-2 d-block mx-auto">
                  Save
                </Button>
                {changePasswordStatus.type === "danger" && (
                  <Alert
                    key={"danger"}
                    variant={"danger"}
                    dismissible
                    onClose={() => setChangePasswordStatus({})}
                    className="my-2"
                  >
                    {changePasswordStatus.message}
                  </Alert>
                )}
                {changePasswordStatus.type === "success" && (
                  <Alert
                    key={"success"}
                    variant={"success"}
                    dismissible
                    onClose={() => setChangePasswordStatus({})}
                    className="my-2"
                  >
                    {changePasswordStatus.message}
                  </Alert>
                )}
              </Form.Group>
            </Form>
          </Col>
        </Row>
      </Container>
    </>
  );
}
