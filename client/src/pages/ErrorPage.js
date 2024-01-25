import { Container, Row, Col, Badge } from 'react-bootstrap';
import { ReactComponent as TransparentLogo } from "../logo.svg";
import { ReactComponent as Logo } from '../logo.svg';
import { useNavigate } from 'react-router-dom';

function ErrorPage() {
    const navigate = useNavigate();

    return <Container fluid className='myLogCol' style={{ height: "100vh", color: "white" }} >
        <Row className='pt-5 text-center'>
            <Col><h1><strong>Error - 404 Page not found</strong></h1></Col>
        </Row>
        <Row  className='mt-5' >
            <Col className='mt-5 text-center'>
                <Row>
                    <Col className='mt-5 mx-5'>
                        <p><h3>The page you were looking for might have been moved, deleted, or it could be hiding somewhere else.</h3>
                        </p> <h3>Don't worry; even the best explorers get lost sometimes.</h3>
                    </Col>
                </Row>
                <Row>
                    <Col className='my-3'>
                        <h2>
                            <Badge bg="light" text='dark' style={{ cursor: "pointer" }} onClick={() => navigate('/home')}>
                                Go to HomePage
                            </Badge>
                        </h2>
                    </Col>
                </Row>
            </Col>
            <Col align='right' className='p-0'><TransparentLogo style={{ opacity: "0.1", height: "35rem" }} /></Col>
        </Row>

    </Container>
}

export default ErrorPage;