import 'bootstrap/dist/css/bootstrap.min.css';
import {Container, Button, Form, Row, Col, Alert} from "react-bootstrap";
import React, {createContext, useState} from "react";
import logo from './logo.svg'
import axios from "axios";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import OpenTicket from "./pages/OpenTicket";
import Login from "./pages/Login";
import Warranty from "./pages/Warranty";


export const Authentication = createContext(null);

function App() {

    const [authentication, setAuthentication] = useState(null);

    return (
        <BrowserRouter>
            <Authentication.Provider value={{authentication, setAuthentication}}>
                <Routes>
                    <Route path="/login" element={<Login/>}>
                    </Route>
                    <Route path="/signup" element={<Login/>}>
                    </Route>
                    <Route path="/open-ticket" element={<OpenTicket/>}>
                    </Route>
                    <Route path="/warranty" element={<Warranty/>}>
                    </Route>
                    <Route exact path="/" element={<Navigate to="/login" replace={true}/>}>
                    </Route>
                </Routes>
            </Authentication.Provider>
        </BrowserRouter>

    );
}

export default App;