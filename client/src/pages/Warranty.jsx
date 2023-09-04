//import 'bootstrap/dist/css/bootstrap.min.css';
import {Alert, Button, Container, Form, Row, Table} from "react-bootstrap";
import {useEffect, useState} from "react";
import logo from '../logo.svg'
import {Message, Ticket, Warranty} from '../models'; // eslint-disable-line no-unused-vars
import API from "../api/api";
import {useAuth} from "../hooks/auth";
import {Link} from "react-router-dom";
import NavigationBar from "./NavigationBar";

/**
 *
 * @returns {Warranty[]}
 */
const initialWarranties = () => {
    // Giusto per avere la type annotation su warranties
    return []
}

function WarrantyPage() {


    const [warranties, setWarranties] = useState(initialWarranties());

    const [associationError, setAssociationError] = useState("");

    //TODO remove it!
    const [messageList, setMessageList] = useState([]);

    const {profile} = useAuth();

    const getWarranties = async () => {
        try {
            const warrantiesRetrieved = await API.WarrantyAPI.getWarranties()
            setWarranties(warrantiesRetrieved)
        } catch (e) {
            console.error(e)
        }
    }

    const testGetMessages = async () => {
        try{
            const messagesRetrieved = await API.MessageAPI.getMessages(55);
            console.log(messagesRetrieved)
            setMessageList(messagesRetrieved)
        }
        catch (e) {

        }
    }


    useEffect(() => {
        getWarranties();
    }, []);

    const testTicketCreation = async () => {
        try {
            const ticket = new Ticket(undefined, undefined, undefined, profile.email, undefined, "0194252708002", "1");
            await API.TicketAPI.ticketCreation(ticket);
        } catch (e) {
            console.error(e)
        }
    }

    const testSendMessage = async () => {
        try {
            const message = new Message(new Date().toString(), "customeruser@gmail.com", 55)
            await API.MessageAPI.sendMessage(message);
        } catch (e) {
            console.error(e)
        }
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        event.stopPropagation();

        setAssociationError("")

        try {
            await API.WarrantyAPI.associateSale(event.target.warrantyId.value)
            event.target.warrantyId.value = ""
            getWarranties()

        } catch (e) {
            setAssociationError(e.data.detail)
        }
    }

    if (profile === null || profile === undefined) {
        return (<>
            <Container>
                <Row className={"my-2"}>
                    <img src={logo} height={"80"} width={"100%"} alt={"Ticket34 Logo"}/>
                </Row>
                <p className={"fs-5 my-2 mx-auto"}>
                    You must be logged in to see this page!
                    <br/>
                    <Link to={"/login"}>Login</Link>
                </p>
            </Container>
        </>)
    }
    return (
        <>
            <NavigationBar/>
            <Container>
                <p className={"fs-5 my-2"}>
                    Your warranties
                </p>
                <Table striped bordered>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Product</th>
                        <th>End</th>
                    </tr>
                    </thead>
                    <tbody>
                    {warranties.map(
                        (w) => <tr>
                            <td>{w.id}</td>
                            <td>{w.product.model}</td>
                            <td>{new Date(Date.parse(w.warranty_end)).toLocaleString()}</td>
                        </tr>
                    )}
                    </tbody>
                </Table>
                <Row className={"my-2"}>
                    <p className="fs-4 my-2">Here you can associate your Warranty to your profile.
                        Please insert the Warranty ID you received when you bought your device!</p>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3" controlId="formWarrantyId">
                            <Form.Label>Associate your warranty</Form.Label>
                            <Form.Control name="warrantyId" required type="text"
                                          placeholder="Enter your warranty identifier"/>
                        </Form.Group>
                        {associationError !== "" &&
                            <Alert key={'danger'} variant={'danger'} className="my-2">
                                {associationError}
                            </Alert>
                        }
                        <Button variant="primary" type="submit">
                            Associate Warranty
                        </Button>
                    </Form>
                    {messageList.length != 0 && <Row>{messageList.map((m)=>m.text)}</Row>}
                </Row>
            </Container>
        </>
    );
}

export default WarrantyPage;