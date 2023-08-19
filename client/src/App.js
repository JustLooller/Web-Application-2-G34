import {createBrowserRouter, createRoutesFromElements, Route, RouterProvider} from "react-router-dom";
import Home from "./pages/Home";
import {AuthProvider} from "./hooks/auth";

function App() {

    const router = createBrowserRouter(
        createRoutesFromElements(
            <Route path={"/"} element={<Home/>}/>
        )
    )
    return (
        <AuthProvider>

            <RouterProvider router={router}/>
        </AuthProvider>
    );
}

export default App;
