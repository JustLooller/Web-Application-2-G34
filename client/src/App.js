import {createBrowserRouter, createRoutesFromElements, Navigate, Route, RouterProvider} from "react-router-dom";
import {AuthProvider} from "./hooks/auth";
import Login from "./pages/Login";
import TicketDetails from "./pages/TicketDetails";
import Warranty from "./pages/Warranty";
import Home from "./pages/Home";
import AdminConsole from "./pages/AdminConsole";
import {useEffect} from "react";
import axios from "axios";
import CreateTicket from "./pages/CreateTicket";
import './App.scss'
import OpenTickets from "./pages/OpenTickets";
import ClosedTickets from "./pages/ClosedTickets";
import Profile from "./pages/Profile";
import Register from "./pages/Register";
import TicketRecap from "./pages/TicketRecap";

const router = createBrowserRouter(
    createRoutesFromElements(
        <>
            <Route path="/login" element={<Login/>}> </Route>
            <Route path="/signup" element={<Register/>}></Route>
            <Route path="/ticket-details/:id" element={<TicketDetails/>}></Route>
            <Route path="/ticket-recap/:id" element={<TicketRecap/>}></Route>
            <Route path="/warranty" element={<Warranty/>}></Route>
            <Route path="/home" element={<Home/>}></Route>
            <Route path="/admin" element={<AdminConsole/>}></Route>
            <Route path="/create-ticket" element={<CreateTicket/>}></Route>
            <Route path="/open-tickets" element={<OpenTickets/>}></Route>
            <Route path="/closed-tickets" element={<ClosedTickets/>}></Route>
            <Route path="/profile" element={<Profile/>}></Route>
            <Route exact path="/" element={<Navigate to="/login" replace={true}/>}></Route>
        </>
    )
)
function App() {
    useEffect(() => {
        axios.defaults.headers["Access-Control-Allow-Origin"] = "*"
    }, [])
    return (
        <AuthProvider>
            <RouterProvider router={router}/>
        </AuthProvider>
    );
}

export default App;
