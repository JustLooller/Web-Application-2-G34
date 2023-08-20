import {createBrowserRouter, createRoutesFromElements, Navigate, Route, RouterProvider} from "react-router-dom";
import {AuthProvider} from "./hooks/auth";
import Login from "./pages/Login";
import OpenTicket from "./pages/OpenTicket";
import Warranty from "./pages/Warranty";
import {useEffect} from "react";
import axios from "axios";

const router = createBrowserRouter(
    createRoutesFromElements(
        <>
            <Route path="/login" element={<Login/>}> </Route>
            <Route path="/signup" element={<Login/>}></Route>
            <Route path="/open-ticket" element={<OpenTicket/>}></Route>
            <Route path="/warranty" element={<Warranty/>}></Route>
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
