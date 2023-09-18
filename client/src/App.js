import {createBrowserRouter, createRoutesFromElements, Navigate, Route, RouterProvider} from "react-router-dom";
import {AuthProvider} from "./hooks/auth";
import Login from "./pages/Login";
import OpenTicket from "./pages/OpenTicket";
import Warranty from "./pages/Warranty";
import Home from "./pages/Home";
import AdminConsole from "./pages/AdminConsole";
import {useEffect} from "react";
import axios from "axios";
import Profile from "./pages/Profile";


const router = createBrowserRouter(
    createRoutesFromElements(
        <>
            <Route path="/login" element={<Login/>}> </Route>
            <Route path="/signup" element={<Login/>}></Route>
            <Route path="/open-ticket" element={<OpenTicket/>}></Route>
            <Route path="/warranty" element={<Warranty/>}></Route>
            <Route path="/home" element={<Home/>}></Route>
            <Route path="/admin" element={<AdminConsole/>}></Route>
            <Route path="/profile" element={<Profile/>}></Route>
            <Route exact path="/" element={<Navigate to="/home" replace={true}/>}></Route>
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
