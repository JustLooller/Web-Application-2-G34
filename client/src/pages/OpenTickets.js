//import 'bootstrap/dist/css/bootstrap.min.css';
import { Button, Container, Row, Table } from "react-bootstrap";
import { useEffect, useState } from "react";
import API from "../api/api";
import { useAuth } from "../hooks/auth";
import NavigationBar from "./NavigationBar";
import { Navigate, useNavigate } from "react-router-dom";
import LoadingSpinner from "../components/LoadingSpinner/LoadingSpinner";

//import './../custom.css';

function OpenTickets() {
  const { profile } = useAuth();
  const [isRetrieving, setIsRetrieving] = useState(false);
  const [tickets, setTickets] = useState([]);

  const navigate = useNavigate();

  const getTickets = async () => {
    try {
      const tickets = await API.TicketAPI.getTickets();
      setTickets(tickets.filter((t) => t.state !== "CLOSED"));
    } catch (e) {
      console.error(e);
    }
  };

  const openTicketDetails = (ticketID) => {
    navigate(`/ticket-details/${ticketID}`, { replace: false });
  };

  useEffect(() => {
    setIsRetrieving(true);
    getTickets()
      .then(() => setIsRetrieving(false))
      .catch(console.error);
  }, []);

  if (!profile) return <Navigate to={"/login"}></Navigate>;

  return (
    <>
      <NavigationBar />
      {isRetrieving ? (
        <LoadingSpinner />
      ) : (
        <Container>
          <Row className="mt-3">
            <Table striped bordered>
              <thead>
                <tr>
                  <th>#</th>
                  <th>State</th>
                  <th>Warranty</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {tickets.map((m) => (
                  <tr>
                    <td>{m.id}</td>
                    <td>{m.state}</td>
                    <td>{m.sale_id}</td>
                    <td>
                      <Button onClick={() => openTicketDetails(m.id)}>
                        DETAILS
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          </Row>
        </Container>
      )}
    </>
  );
}

export default OpenTickets;
