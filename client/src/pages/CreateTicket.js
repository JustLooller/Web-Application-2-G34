//import 'bootstrap/dist/css/bootstrap.min.css';
import {Alert, Button, Container, Form, Row} from "react-bootstrap";
import {useEffect, useState} from "react";
import API from "../api/api";
import {useAuth} from "../hooks/auth";
import {Message, Ticket} from "../models";
import NavigationBar from "./NavigationBar";
import {useNavigate} from "react-router-dom";
//import './../custom.css';

const initialWarranties = () => {
    // Giusto per avere la type annotation su warranties
    return []
}

function CreateTicket() {

    const {profile} = useAuth();

    const [warranties, setWarranties] = useState(initialWarranties());
    const [creationError, setCreationError] = useState(false);

    const navigate = useNavigate();

    const getWarranties = async () => {
        try {
            const warrantiesRetrieved = await API.WarrantyAPI.getWarranties()
            setWarranties(warrantiesRetrieved)
        } catch (e) {
            console.error(e)
        }
    }

    useEffect(() => {
        getWarranties();
    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();
        event.stopPropagation();
        const form = event.currentTarget;
        const selectedWarranty = warranties.filter((w)=>w.id === form.warranty.value)[0]
        console.log(selectedWarranty.product.ean)
        console.log(selectedWarranty.id)

        const ticket = new Ticket(undefined, undefined, undefined, profile.email, undefined, selectedWarranty.product.ean, selectedWarranty.id);
        try {
            const ticketResponse = await API.TicketAPI.ticketCreation(ticket)
            const createdTicket = Ticket.fromJson(ticketResponse)

            const message = new Message(form.message.value, profile.email, createdTicket.id)
            await API.MessageAPI.sendMessage(message);
    
            navigate(`/ticket-details/${createdTicket.id}`, { replace: false });
        }
        catch (e) {
            console.error(e)
            setCreationError(true)
        }
    }


    return (
        <>
            <NavigationBar/>
            <Container>
                <Row className="mt-3">
                    {creationError && <Alert variant="danger">An open ticket associated with this warranty already exists</Alert>}
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Select the warranty:</Form.Label>
                            <Form.Control as="select" required name="warranty">
                                <option value="">Select Warranty</option>
                                {warranties.map((w) => <option value={w.id}>{w.product.model} - {w.id}</option>)}
                            </Form.Control>
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>Describe your problem</Form.Label>
                            <Form.Control placeholder="Message" required name="message"/>
                        </Form.Group>
                        <Button type="submit">Open Ticket</Button>
                    </Form>
                </Row>
            </Container>
        </>
    );
}

export default CreateTicket;