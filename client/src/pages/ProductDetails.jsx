import React from 'react';
import { Container, Row, Col, Image } from 'react-bootstrap';

export default function ProductDetail(product){


  return (
    <Container>
      <Row>
        <Col>
          <h1 className="text-center">{product.name}</h1>
          <Image src={product.imageUrl} alt={product.name} fluid />
          <p className="mb-3">EAN: {product.ean}</p>
          <p className="mb-3">Brand: {product.brand}</p>
        </Col>
        <Col>
          <p className="bg-light p-3">Description: {product.description}</p>
        </Col>
      </Row>
    </Container>
  );
};