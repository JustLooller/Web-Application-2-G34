import 'bootstrap/dist/css/bootstrap.min.css';
import {Container, Button, Form, Row, Col, Alert, Table} from "react-bootstrap";
import {useContext, useEffect, useState} from "react";
import logo from '../logo.svg'
import axios from "axios";
import {Navigate} from "react-router-dom";
import {Authentication} from "../App";


function Warranty() {

    const [warranties, setWarranties] = useState([]);

    //test useContext
    //const {authentication, setAuthentication} = useContext(Authentication);
    const authentication = JSON.parse(localStorage.getItem("authentication"))

    useEffect(() => {
        const config = {
            headers: {Authorization: `Bearer ${authentication.access_token}`}
        };
        axios.get("http://localhost:8081/api/sale", config).then(
            (response)=>setWarranties(
                response.data.map((w)=>{return {id:w.id, product:w.product.brand.name + " " + w.product.model, end:w.warranty_end}})
            )
        )

    }, []);

    const handleSubmit = async (event) => {
        event.preventDefault();
        event.stopPropagation();
        console.log("auth token " + authentication.access_token)
        console.log("url " + 'http://localhost:8081/api/sale/'+ event.target.warrantyId.value)
        const config = {
            headers: {Authorization: `Bearer ${authentication.access_token}`}
        };
        console.log("config: " + config.headers.Authorization)
        try{
            const response = await axios.post('http://localhost:8081/api/sale/'+ event.target.warrantyId.value,null,config)
            console.log(response)
        }
        catch (e) {
            console.log(e.)
        }

    }

    return (
        <>
            <Container>
                <Row className={"my-2"}>
                    <img src={logo} height={"80"} width={"100%"}/>
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
                        (w)=><tr><td>{w.id}</td><td>{w.product}</td><td>{new Date(Date.parse(w.end)).toLocaleString()}</td></tr>
                    )}
                    </tbody>
                </Table>
                <Row className={"my-2"}>
                    <p className="fs-4 my-2">Here you can associate your Warranty to your profile.
                        Please insert the Warranty ID you received when you bought your device!</p>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-3" controlId="formWarrantyId">
                            <Form.Label>Associate your warranty</Form.Label>
                            <Form.Control name="warrantyId" required type="text" placeholder="Enter your warranty identifier"/>
                        </Form.Group>
                        <Button variant="primary" type="submit">
                            Associate Warranty
                        </Button>
                    </Form>
                </Row>
            </Container>
        </>
    );
}

export default Warranty;