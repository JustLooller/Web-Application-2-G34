//import 'bootstrap/dist/css/bootstrap.min.css';
import {Alert, Button, Col, Container, Form, Row, Stack, Table} from "react-bootstrap";
import logo from "../logo.svg";
import {useEffect, useState} from "react";
import API from "../api/api";
import {useAuth} from "../hooks/auth";
import {Message, Ticket} from "../models";
import NavigationBar from "./NavigationBar";
import {Navigate, useNavigate} from "react-router-dom";
//import './../custom.css';

function ClosedTickets() {

    const {profile} = useAuth();

    const [tickets, setTickets] = useState([]);

    const navigate = useNavigate();

    const getTickets = async () => {
        try {
            const tickets = await API.TicketAPI.getTickets()
            setTickets(tickets.filter(t=>t.state === "CLOSED"))
        } catch (e) {
            console.error(e)
        }
    }

    const openTicketChat = (ticketID)=>{
        navigate(`/ticket-details/${ticketID}`, { replace: false });
    }
    const openTicketRecap = (ticketID)=>{
        navigate(`/ticket-recap/${ticketID}`, { replace: false });
    }
    useEffect(() => {
        getTickets();
    }, []);

    if (!profile) return <Navigate to={"/login"}></Navigate>;

    return (
        <>
            <NavigationBar/>
            <Container>
                <Row className="mt-3">
                    <Table striped bordered>
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>State</th>
                            <th>Warranty</th>
                            <th>Go To</th>
                        </tr>
                        </thead>
                        <tbody>
                        {tickets.map(
                            (m) => <tr>
                                <td>{m.id}</td>
                                <td>{m.state}</td>
                                <td>{m.sale_id}</td>
                                <td>
                                    <Stack direction="horizontal">
                                        <Button className="mx-auto" onClick={() => openTicketChat(m.id)}>
                                            Chat
                                        </Button>
                                        <div className={'vr'}></div>
                                        <Button className="mx-auto" onClick={() => openTicketRecap(m.id)}>
                                            Info
                                        </Button>
                                    </Stack>
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </Table>
                </Row>
            </Container>
        </>
    );
}

export default ClosedTickets;