import 'bootstrap/dist/css/bootstrap.min.css';
import {Alert, Button, Container, Form, Row, Table} from "react-bootstrap";
import {useEffect, useState} from "react";
import logo from '../logo.svg'
import {Warranty} from '../models'; // eslint-disable-line no-unused-vars
import API from "../api/api";
import {useAuth} from "../hooks/auth";
import {Link} from "react-router-dom";

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

    const {profile} = useAuth();


    useEffect(() => {
        const getWarranties = async () => {
            try {
                const warrantiesRetrieved = await API.WarrantyAPI.getWarranties()
                setWarranties(warrantiesRetrieved)
            } catch (e) {
                console.error(e)
            }
        }
        getWarranties();
    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();
        event.stopPropagation();

        try {
            const warrantyRetrieved = await API.WarrantyAPI.associateSale(event.target.warrantyId.value)
            console.log(warrantyRetrieved);

        } catch (e) {
            console.error(e)
            setAssociationError(e.message)
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
            <Container>
                <Row className={"my-2"}>
                    <img src={logo} height={"80"} width={"100%"} alt={"Ticket34 Logo"}/>
                </Row>
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
                </Row>
            </Container>
        </>
    );
}

export default WarrantyPage;