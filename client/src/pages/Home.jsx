import '../App.scss';
import {Link} from 'react-router-dom';
import NavigationBar from './NavigationBar'
function Home() {
    return <>
        <NavigationBar/>
        <div className="container mt-5">
            <div className="row">
                <div className="col-md-12 text-center mb-4">
                    <h1>Welcome to TICKET34!</h1>
                    <br/>
                    <p> Generate Your Tickets, Monitor Active Cases, and Review Resolved Matters</p>
                    <p>All in One Place</p>
                </div>
            </div>
            <div className="row">
                <div className="col-md-3 d-flex">
                    <Link className='myHomeLink' to="/create-ticket">
                        <div className="card shadow-sm mb-4 text-center d-flex flex-column myHomeCard" >
                            <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" fill="currentColor"
                                 className="mx-auto my-4 bi bi-clipboard2-plus" viewBox="0 0 16 16">
                                <path
                                    d="M9.5 0a.5.5 0 0 1 .5.5.5.5 0 0 0 .5.5.5.5 0 0 1 .5.5V2a.5.5 0 0 1-.5.5h-5A.5.5 0 0 1 5 2v-.5a.5.5 0 0 1 .5-.5.5.5 0 0 0 .5-.5.5.5 0 0 1 .5-.5h3Z"/>
                                <path
                                    d="M3 2.5a.5.5 0 0 1 .5-.5H4a.5.5 0 0 0 0-1h-.5A1.5 1.5 0 0 0 2 2.5v12A1.5 1.5 0 0 0 3.5 16h9a1.5 1.5 0 0 0 1.5-1.5v-12A1.5 1.5 0 0 0 12.5 1H12a.5.5 0 0 0 0 1h.5a.5.5 0 0 1 .5.5v12a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5v-12Z"/>
                                <path
                                    d="M8.5 6.5a.5.5 0 0 0-1 0V8H6a.5.5 0 0 0 0 1h1.5v1.5a.5.5 0 0 0 1 0V9H10a.5.5 0 0 0 0-1H8.5V6.5Z"/>
                            </svg>
                            <div className="card-body mx-4">
                                <h5 className="card-title">New Ticket</h5>
                                <p className="card-text">Create your ticket with ease, ensuring you have your warranty code and all your product specifics ready</p>
                            </div>
                        </div>
                    </Link>
                </div>
                <div className="col-md-3 d-flex">
                    <Link className='myHomeLink' to="/open-tickets">
                        <div className="card shadow-sm mb-4 text-center d-flex flex-column myHomeCard">
                            <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" fill="currentColor"
                                 className="mx-auto my-4 bi bi-clipboard2-heart" viewBox="0 0 16 16">
                                <path
                                    d="M10.058.501a.501.501 0 0 0-.5-.501h-2.98c-.276 0-.5.225-.5.501A.499.499 0 0 1 5.582 1a.497.497 0 0 0-.497.497V2a.5.5 0 0 0 .5.5h4.968a.5.5 0 0 0 .5-.5v-.503A.497.497 0 0 0 10.555 1a.499.499 0 0 1-.497-.499Z"/>
                                <path
                                    d="M3.605 2a.5.5 0 0 0-.5.5v12a.5.5 0 0 0 .5.5h9a.5.5 0 0 0 .5-.5v-12a.5.5 0 0 0-.5-.5h-.5a.5.5 0 0 1 0-1h.5a1.5 1.5 0 0 1 1.5 1.5v12a1.5 1.5 0 0 1-1.5 1.5h-9a1.5 1.5 0 0 1-1.5-1.5v-12a1.5 1.5 0 0 1 1.5-1.5h.5a.5.5 0 0 1 0 1h-.5Z"/>
                                <path d="M8.068 6.482c1.656-1.673 5.795 1.254 0 5.018-5.795-3.764-1.656-6.69 0-5.018Z"/>
                            </svg>
                            <div className="card-body mx-4">
                                <h5 className="card-title">Open Tickets</h5>
                                <p className="card-text">Monitor your tickets, stay informed about progress, read new messages, and let us take care of the rest.</p>
                            </div>
                        </div>
                    </Link>
                </div>
                <div className="col-md-3 d-flex">
                    <Link className='myHomeLink' to="/closed-tickets">
                        <div className="card shadow-sm mb-4 text-center d-flex flex-column myHomeCard">
                            <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" fill="currentColor"
                                 className="mx-auto my-4 bi bi-clipboard2-minus" viewBox="0 0 16 16">
                                <path
                                    d="M9.5 0a.5.5 0 0 1 .5.5.5.5 0 0 0 .5.5.5.5 0 0 1 .5.5V2a.5.5 0 0 1-.5.5h-5A.5.5 0 0 1 5 2v-.5a.5.5 0 0 1 .5-.5.5.5 0 0 0 .5-.5.5.5 0 0 1 .5-.5h3Z"/>
                                <path
                                    d="M3 2.5a.5.5 0 0 1 .5-.5H4a.5.5 0 0 0 0-1h-.5A1.5 1.5 0 0 0 2 2.5v12A1.5 1.5 0 0 0 3.5 16h9a1.5 1.5 0 0 0 1.5-1.5v-12A1.5 1.5 0 0 0 12.5 1H12a.5.5 0 0 0 0 1h.5a.5.5 0 0 1 .5.5v12a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5v-12Z"/>
                                <path d="M6 8a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1H6Z"/>
                            </svg>
                            <div className="card-body mx-4">
                                <h5 className="card-title">Closed Tickets</h5>
                                <p className="card-text">Review past resolutions, appreciate our dedicated service, and experience the peace of mind we deliver.</p>
                            </div>
                        </div>
                    </Link>
                </div>
                <div className="col-md-3 d-flex">
                    <Link className='myHomeLink' to="/warranty">
                        <div className="card shadow-sm mb-4 text-center d-flex flex-column myHomeCard">
                            <svg xmlns="http://www.w3.org/2000/svg" width="60" height="60" fill="currentColor"
                                 className="mx-auto my-4 bi bi-clipboard2-minus" viewBox="0 0 16 16">
                                <path
                                    d="M9.5 0a.5.5 0 0 1 .5.5.5.5 0 0 0 .5.5.5.5 0 0 1 .5.5V2a.5.5 0 0 1-.5.5h-5A.5.5 0 0 1 5 2v-.5a.5.5 0 0 1 .5-.5.5.5 0 0 0 .5-.5.5.5 0 0 1 .5-.5h3Z"/>
                                <path
                                    d="M3 2.5a.5.5 0 0 1 .5-.5H4a.5.5 0 0 0 0-1h-.5A1.5 1.5 0 0 0 2 2.5v12A1.5 1.5 0 0 0 3.5 16h9a1.5 1.5 0 0 0 1.5-1.5v-12A1.5 1.5 0 0 0 12.5 1H12a.5.5 0 0 0 0 1h.5a.5.5 0 0 1 .5.5v12a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5v-12Z"/>
                                <path d="M6 8a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1H6Z"/>
                            </svg>
                            <div className="card-body mx-4">
                                <h5 className="card-title">Add Warranties</h5>
                                <p className="card-text">Associate your warranty to your account.</p>
                            </div>
                        </div>
                    </Link>
                </div>
            </div>
        </div>
    </>
}


export default Home;