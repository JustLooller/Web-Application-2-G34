import {createBrowserRouter, createRoutesFromElements, Navigate, Route, RouterProvider} from "react-router-dom";
import {AuthProvider} from "./hooks/auth";
import Login from "./pages/Login";
import OpenTicket from "./pages/OpenTicket";
import Warranty from "./pages/Warranty";
import Home from "./pages/Home";
import AdminConsole from "./pages/AdminConsole";
import {useEffect} from "react";
import axios from "axios";
import CreateTicket from "./pages/CreateTicket";
import './App.scss'

const router = createBrowserRouter(
    createRoutesFromElements(
        <>
            <Route path="/login" element={<Login/>}> </Route>
            <Route path="/signup" element={<Login/>}></Route>
            <Route path="/open-ticket/:id" element={<OpenTicket/>}></Route>
            <Route path="/warranty" element={<Warranty/>}></Route>
            <Route path="/home" element={<Home/>}></Route>
            <Route path="/admin" element={<AdminConsole/>}></Route>
            <Route path="/create-ticket" element={<CreateTicket/>}></Route>
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
