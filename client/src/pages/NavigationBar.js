import {Link, useNavigate} from "react-router-dom";
import {
    Navbar,
    Dropdown,
    Button,
} from "react-bootstrap";
import {useAuth} from "../hooks/auth";
import logo from "../logo.svg";

/**
 * NavBar: contain site-logo, site-name, user-icon, button for logout
 * @param {*} props
 * @returns NavigationBar
 */
function NavigationBar() {
    /**
     * During Logout, all components are reinitialized, including the statuses of: mode, students logged in.
     * The number of credits and the course vector are also reset.
     */
    const {profile, logout} = useAuth();
    const navigator = useNavigate();
    const handleLogout = async (event) => {
        event.preventDefault();
        logout();
        navigator("/login");
    };

    return (
        <>
            <Navbar
                variant="light"
                expand="md"
                sticky="top"
                className="navbar-padding py-1 py-sm-3 myNavbar"
            >
                <div className="col-3">
                    <Dropdown className="ms-1">
                        <Dropdown.Toggle
                            className="bg-transparent myNavbarToggle"
                            id="dropdown-basic"
                            variant="light"
                        >
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                width="30"
                                height="30"
                                fill="currentColor"
                                className="mb-2 mx-2 bi bi-person-square"
                                viewBox="0 0 16 16"
                            >
                                <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
                                <path
                                    d="M2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2zm12 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1v-1c0-1-1-4-6-4s-6 3-6 4v1a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12z"/>
                            </svg>
                        </Dropdown.Toggle>
                        <Dropdown.Menu>
                            <Dropdown.Item as={Link} to="/profile">
                                <svg
                                    xmlns="http://www.w3.org/2000/svg"
                                    width="18"
                                    height="18"
                                    fill="currentColor"
                                    className="bi bi-person-lines-fill mx-1 mb-1"
                                    viewBox="0 0 16 16"
                                >
                                    <path
                                        d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm-5 6s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zM11 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5zm.5 2.5a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1h-4zm2 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2zm0 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2z"/>
                                </svg>
                                User Details
                            </Dropdown.Item>
                            {profile ? (
                                <Dropdown.Item onClick={handleLogout}>
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        width="18"
                                        height="18"
                                        fill="currentColor"
                                        className="bi bi-door-open mx-1 mb-1"
                                        viewBox="0 0 16 16"
                                    >
                                        <path d="M8.5 10c-.276 0-.5-.448-.5-1s.224-1 .5-1 .5.448.5 1-.224 1-.5 1z"/>
                                        <path
                                            d="M10.828.122A.5.5 0 0 1 11 .5V1h.5A1.5 1.5 0 0 1 13 2.5V15h1.5a.5.5 0 0 1 0 1h-13a.5.5 0 0 1 0-1H3V1.5a.5.5 0 0 1 .43-.495l7-1a.5.5 0 0 1 .398.117zM11.5 2H11v13h1V2.5a.5.5 0 0 0-.5-.5zM4 1.934V15h6V1.077l-6 .857z"/>
                                    </svg>
                                    Logout
                                </Dropdown.Item>
                            ) : (
                                <Dropdown.Item as={Link} to="/login">
                                    <svg
                                        xmlns="http://www.w3.org/2000/svg"
                                        width="18"
                                        height="18"
                                        fill="currentColor"
                                        className="bi bi-door-open mx-1 mb-1"
                                        viewBox="0 0 16 16"
                                    >
                                        <path
                                            fillRule="evenodd"
                                            d="M8 1.5a.5.5 0 0 1 .5.5v2.5a.5.5 0 0 1-1 0V2a.5.5 0 0 1 .5-.5z"
                                        />
                                        <path
                                            fillRule="evenodd"
                                            d="M2.5 6.5A2.5 2.5 0 0 1 5 4h6a2.5 2.5 0 0 1 2.5 2.5v7.5a.5.5 0 0 1-1 0V7A1.5 1.5 0 0 0 11 5.5H5A1.5 1.5 0 0 0 3.5 7v7.5a.5.5 0 0 1-1 0V7z"
                                        />
                                        <path
                                            fillRule="evenodd"
                                            d="M13.5 2.5a.5.5 0 0 1 1 0v2.793a.5.5 0 0 1-.224.416l-6 3.5a.5.5 0 0 1-.552 0l-6-3.5A.5.5 0 0 1 2 5.293V2.5a.5.5 0 0 1 1 0v2.793l5 2.917 5-2.917V2.5z"
                                        />
                                    </svg>
                                    Login
                                </Dropdown.Item>
                            )}
                        </Dropdown.Menu>
                    </Dropdown>
                </div>
                <Navbar.Brand as={Link} to="/home" className="mx-auto">
                    <img src={logo} alt="Logo" className="myNavbarLogo"/>
                </Navbar.Brand>

                <div className="col-3 justify-content-end d-none d-sm-flex">
                    {profile && (
                        <Navbar.Text>
                            {profile.name}
                        </Navbar.Text>
                    )}
                    <Button
                        onClick={handleLogout}
                        className="bg-transparent myNavbarToggle"
                        id="dropdown-basic"
                        variant="light"
                    >
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            width="30"
                            height="30"
                            fill="currentColor"
                            className="mb-2 mx-2 bi bi-door-open mx-1 mb-1"
                            viewBox="0 0 16 16"
                        >
                            <path d="M8.5 10c-.276 0-.5-.448-.5-1s.224-1 .5-1 .5.448.5 1-.224 1-.5 1z"/>
                            <path
                                d="M10.828.122A.5.5 0 0 1 11 .5V1h.5A1.5 1.5 0 0 1 13 2.5V15h1.5a.5.5 0 0 1 0 1h-13a.5.5 0 0 1 0-1H3V1.5a.5.5 0 0 1 .43-.495l7-1a.5.5 0 0 1 .398.117zM11.5 2H11v13h1V2.5a.5.5 0 0 0-.5-.5zM4 1.934V15h6V1.077l-6 .857z"/>
                        </svg>
                    </Button>
                </div>
            </Navbar>
        </>
    );
}

export default NavigationBar;
