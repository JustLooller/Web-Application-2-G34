import {Navigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {StateHistory, Ticket, TicketActions} from "../models";
import NavigationBar from "./NavigationBar";
import {Alert, Button, Container, Row, Spinner, Table} from "react-bootstrap";
import API from "../api/api";
import {useAuth} from "../hooks/auth";
// import dayjs from "dayjs";

/**
 *  Just for type hinting
 *  @return {Ticket}
 *  */
const typeHintTicket = () => (null);

/**
 * Just for type hinting
 * @return {StateHistory[]}
 */
const typeHintStateHistory = () => ([]);

export default function TicketRecap() {

    const {profile} = useAuth();
    const {id} = useParams();
    const [ticketDetails, setTicketDetails] = useState(typeHintTicket());
    const [ticketHistory, setTicketHistory] = useState(typeHintStateHistory());
    const [isLoading, setIsLoading] = useState(true);

    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        const retrieve = async () => {
            /**
             * @type {[Ticket, StateHistory[]]} tuple
             */
            try {
                const [ticket, stateHistory] = await Promise.all([API.TicketAPI.getTicketById(id), API.TicketAPI.getTicketHistory(id)])
                setTicketDetails(ticket);
                setTicketHistory(stateHistory);
                setIsLoading(false);
            } catch (e) {
                console.error(e);
            }
        }
        retrieve();
    }, []);

    const executeAction = (ticketAction) => {

    }
    if (!profile || profile.role === "CUSTOMER") {
        return <Navigate to={"/"}/>
    }

    if (isLoading) {
        return (<>
            <NavigationBar/>
            <Spinner animation="grow" role="status">
                <span className="visually-hidden">Loading...</span>
            </Spinner>
        </>);
    }

    return (<>
        <NavigationBar/>
        <Container className={"mt-2"}>

            <Table striped bordered hover>
                <thead>
                <tr>
                    <th colSpan={4} className={"text-center"}>Ticket Details</th>
                </tr>
                <tr>
                    <th>#</th>
                    <th>Status</th>
                    <th>Expert</th>
                    <th>Warranty</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>{ticketDetails.id}</td>
                    <td>{ticketDetails.state}</td>
                    <td>{(ticketDetails.expert_mail === null) ? "NOT ASSIGNED" : ticketDetails.expert_mail}</td>
                    <td>{ticketDetails.sale_id}</td>
                </tr>
                </tbody>
            </Table>
            <Row className={"justify-content-around py-2 my-2"}>
                {Object.entries(TicketActions).map(([key, value]) => {
                    return <Button className={"col-auto"} key={key} variant="primary" onClick={async () => {
                        executeAction(key)
                    }}>{key}</Button>
                })}
            </Row>
            <Table striped bordered hover>
                <thead>
                <tr>
                    <th colSpan={4} className={"text-center"}>Ticket History</th>
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
                    return <tr key={index + 1}>
                        <td>{index + 1}</td>
                        <td>{stateHistory.status}</td>
                        <td>{new Date(stateHistory.timestamp).toDateString()}</td>
                        <td>{stateHistory.user_mail}</td>
                    </tr>
                })}
                </tbody>
            </Table>

            {errorMessage !== "" && <Alert dismissible variant="danger">{errorMessage}</Alert>}
        </Container>
    </>);
}
