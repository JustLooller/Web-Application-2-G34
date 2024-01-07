import {Navigate, useParams} from "react-router-dom";
import {useEffect, useMemo, useState} from "react";
import {StateHistory, Ticket, Product, TicketActions} from "../models";
import NavigationBar from "./NavigationBar";
import {Alert, Button, Col, Container, FloatingLabel, Form, Row, Table} from "react-bootstrap";
import API from "../api/api";
import {useAuth} from "../hooks/auth";
import LoadingSpinner from "../components/LoadingSpinner/LoadingSpinner";


const ActionButton = ({label, onClick}) => {
    const [email, setEmail] = useState("");
    const [priority, setPriority] = useState("LOW");

    if (label === "START") {
        return (<>
            <Form style={{width: "fit-content"}} onSubmit={(e) => {
                e.preventDefault()
                onClick(email, priority)
            }}>
                <Row className={"align-items-center"}>
                    <Col className={"col-auto"}>
                        <Form.Group controlId="formBasicEmail">
                            <FloatingLabel controlId="email" label="Expert's Email">
                            <Form.Control type="email" placeholder="walt@bianchi.com"
                                          onChange={(e) => setEmail(e.target.value)}/>
                            </FloatingLabel>
                        </Form.Group>
                    </Col>
                    <Col className={"col-auto"}>
                        <FloatingLabel controlId="floatingSelect" label="Priority">
                        <Form.Select aria-label="Priority" onChange={(e) => setPriority(e.target.value)}>
                            <option value="LOW">Low</option>
                            <option value="MEDIUM">Medium</option>
                            <option value="HIGH">High</option>
                        </Form.Select>
                        </FloatingLabel>
                    </Col>
                    <Col>
                        <Button variant="primary" type="submit">
                            ASSIGN
                        </Button>
                    </Col>
                </Row>
            </Form>
        </>)
    }
    return (<Button style={{width: "min-content", height: "min-content"}} variant="primary" type="submit"
                    onClick={() => onClick()}>
        {label}
    </Button>)
}


/**
 *  Just for type hinting
 *  @return {Ticket}
 *  */
const typeHintTicket = () => null;

/**
 *  Just for type hinting
 *  @return {Product}
 *  */
const typeHintProduct = () => null;


/**
 * Just for type hinting
 * @return {StateHistory[]}
 */

const typeHintStateHistory = () => [];

export default function TicketRecap() {
    const {profile} = useAuth();
    const {id} = useParams();
    const [ticketDetails, setTicketDetails] = useState(typeHintTicket());
    const [ticketHistory, setTicketHistory] = useState(typeHintStateHistory());
    const [product, setProduct] = useState(typeHintProduct());
    const [isLoading, setIsLoading] = useState(true);

    const [errorMessage, setErrorMessage] = useState("");

    /**
     *
     * @type {string[]}
     */
    const possibleActions = useMemo(() => {
        if (!ticketDetails || !ticketDetails.state) return [];
        let actions = []
        switch (ticketDetails.state) {
            case "OPEN":
                actions =  ["START", "RESOLVE", "CLOSE"];
                break;
            case "IN_PROGRESS":
                actions =  ["OPEN", "CLOSE", "RESOLVE"];
                break;
            case "RESOLVED":
                actions =  ["REOPEN", "CLOSE"];
                break;
            case "REOPENED":
                actions =  ["START", "RESOLVE", "CLOSE"];
                break;
            case "CLOSED":
                actions =  ["REOPEN"];
                break;
        }
        if (profile.role === "EXPERT") {
            actions = actions.filter(action => !["START", "REOPEN"].includes(action))
        }
        else if (profile.role === "MANAGER") {
            actions = actions.filter(action => !["STOP", "RESOLVE", "REOPEN"].includes(action))
        }
        else if (profile.role === "CUSTOMER") {
            actions = actions.filter(action => !["START", "STOP"].includes(action))
        }

        return actions;
    }, [profile, ticketDetails])
    const fetchAll = async () => {
        /**
         * @type {[Ticket, StateHistory[]]} tuple
         */
        try {
            setIsLoading(true)
            const [ticket, stateHistory] = await Promise.all([
                API.TicketAPI.getTicketById(id),
                API.TicketAPI.getTicketHistory(id),
            ]);
            stateHistory.reverse();
            const product = await API.ProductsAPI.get(ticket.product_ean);
            setTicketDetails(ticket);
            setTicketHistory(stateHistory);
            setProduct(product);
            setIsLoading(false);
        } catch (e) {
            console.error(e);
        }
    };

    useEffect(() => {
        fetchAll();
    }, []);

    const executeAction = async (ticketAction) => {
        console.debug("executing following action", ticketAction);
        if (ticketAction === null || !ticketDetails) return;
        setIsLoading(true);
        try {
            const actionDone = await API.TicketAPI.action(ticketDetails.id, ticketAction);

            if (actionDone) {
                console.log("action done")
                setErrorMessage("")
                await fetchAll();
            } else {
                console.log("action not done")
                setIsLoading(false);
                setErrorMessage("Action not done");
            }
        }
        catch (e) {
            console.error(e);
            setIsLoading(false);
            if(e.status === 403) {
                setErrorMessage("You are not allowed to perform this action");
            }
            else if (e.data && e.data.detail)
                setErrorMessage(e.data.detail);
            else
                setErrorMessage("Error while performing action");
            // setErrorMessage(e.data.detail);
        }

    };
    if (!profile) {
        return <Navigate to={"/"}/>;
    }

    if (isLoading) {
        return (
            <>
                <NavigationBar/>
                <Container className={"mt-2"}>
                    <LoadingSpinner></LoadingSpinner>
                </Container>
            </>
        );
    }

    return (
        <>
            <NavigationBar/>
            <Container className={"mt-2"}>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th colSpan={5} className={"text-center"}>
                            Ticket Details
                        </th>
                    </tr>
                    <tr>
                        <th>#</th>
                        <th>Status</th>
                        <th>Expert</th>
                        <th>Priority</th>
                        <th>Warranty</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>{ticketDetails.id}</td>
                        <td>{ticketDetails.state}</td>
                        <td>
                            {ticketDetails.expert_mail === null
                                ? "NOT ASSIGNED"
                                : ticketDetails.expert_mail}
                        </td>
                        <td>{ticketDetails.priority}</td>
                        <td>{ticketDetails.sale_id}</td>
                    </tr>
                    </tbody>
                    <thead>
                    <tr>
                        <th colSpan={5} className={"text-center"}>
                            Product Details
                        </th>
                    </tr>
                    <tr>
                        <th>EAN</th>
                        <th>Brand</th>
                        <th>Model</th>
                        <th colSpan={2}>Description</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>{product.ean}</td>
                        <td>{product.brand.name}</td>
                        <td>{product.model}</td>
                        <td colSpan={2}>{product.description}</td>
                    </tr>
                    </tbody>
                </Table>
                <Row className={"justify-content-around py-2 my-2 align-items-center"}>
                    {Object.entries(TicketActions).filter(([key, _val]) => possibleActions.includes(key)).map(([key, endpoint_callback]) => {
                        return (
                            <ActionButton
                                key={key}
                                label={key}
                                onClick={(opt_value, opt_priority) => {
                                    executeAction(endpoint_callback(opt_value, opt_priority));
                                }
                                }
                            />
                        );
                    })}
                </Row>
                {possibleActions.length === 0 && ( <p style={{width: "100%", textAlign: "center"}}> No possible actions for this ticket </p> )}
                {errorMessage !== "" && (
                    <Alert dismissible variant="danger" onClose={() => setErrorMessage("")}>
                        {errorMessage}
                    </Alert>
                )}
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th colSpan={4} className={"text-center"}>
                            Ticket History
                        </th>
                    </tr>
                    <tr>
                        <th>#</th>
                        <th>State</th>
                        <th>Date</th>
                        <th>User Mail</th>
                    </tr>
                    </thead>
                    <tbody>
                    {ticketHistory.map((stateHistory, index) => {
                        return (
                            <tr key={index + 1}>
                                <td>{ticketHistory.length - index}</td>
                                <td>{stateHistory.status}</td>
                                <td>{new Date(stateHistory.timestamp).toDateString()}</td>
                                <td>{stateHistory.user_mail}</td>
                            </tr>
                        );
                    })}
                    </tbody>
                </Table>
            </Container>
        </>
    );

}
