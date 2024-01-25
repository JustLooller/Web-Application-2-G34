//import 'bootstrap/dist/css/bootstrap.min.css';
import {ButtonGroup, Col, Container, Row, Table, ToggleButton} from "react-bootstrap";
import {useEffect, useState} from "react";
import API from "../api/api";
import {useAuth} from "../hooks/auth";
import NavigationBar from "./NavigationBar";
import {Navigate, useNavigate} from "react-router-dom";

//import './../custom.css';

function WorkersList() {

    const {profile} = useAuth();

    const [workers, setWorkers] = useState([]);

    const [radioValue, setRadioValue] = useState('EXPERT');

    const radios = [
        {name: 'Manager', value: 'MANAGER'},
        {name: 'Expert', value: 'EXPERT'},
    ];


    const getWorkers = async () => {
        try {
            const workers = await API.ProfileAPI.getWorkers()
            setWorkers(workers.filter((p) => p.role === radioValue))
        } catch (e) {
            console.error(e)
        }
    }

    useEffect(() => {
        getWorkers();
    }, [radioValue]);

    if (!profile) return <Navigate to={"/login"}></Navigate>;

    return (
        <>
            <NavigationBar/>
            <Container>
                <Row className="my-2 mt-3 justify-content-end">
                    <Col xs={6}>
                        <p className="text-center">
                            On this page, you can see the list of workers.
                            <br/>
                            Please choose the type of worker you want to
                            view.
                        </p>
                    </Col>
                    <Col xs={6} className="ml-auto">
                        <Row className="mx-5 justify-content-end">
                            <ButtonGroup>
                                {radios.map((radio, idx) => (
                                    <ToggleButton
                                        key={idx}
                                        id={`radio-${idx}`}
                                        type="radio"
                                        variant={'secondary'}
                                        name="radio"
                                        value={radio.value}
                                        checked={radioValue === radio.value}
                                        onChange={(e) => setRadioValue(e.currentTarget.value)}
                                    >
                                        {radio.name}
                                    </ToggleButton>
                                ))}
                            </ButtonGroup>
                        </Row>
                    </Col>
                </Row>

                <Row className="mt-3">
                    <Table striped bordered>
                        <thead>
                        <tr>
                            <th>Email</th>
                            <th>Full Name</th>
                            <th>Role</th>
                        </tr>
                        </thead>
                        <tbody>
                        {workers.map(
                            (m) => <tr>
                                <td>{m.email}</td>
                                <td>{m.name}</td>
                                <td>{m.role}</td>
                            </tr>
                        )}
                        </tbody>
                    </Table>
                </Row>
            </Container>
        </>
    );
}

export default WorkersList;