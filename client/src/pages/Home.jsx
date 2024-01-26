import "../App.scss";
import { Link, Navigate } from "react-router-dom";
import NavigationBar from "./NavigationBar";
import { useAuth } from "../hooks/auth";
import { Role } from "../models";

function Home() {
  const { profile } = useAuth();
  if (!profile) {
    return <Navigate to="/login"></Navigate>;
  }

  return (
    <>
      <NavigationBar />
      <div className="container mt-5">
        <div className="row">
          <div className="col-md-12 text-center mb-4">
            <h1>Welcome to TICKET34!</h1>
            <br />
            <p>
              {" "}
              Generate Your Tickets, Monitor Active Cases, and Review Resolved
              Matters
            </p>
            <p>All in One Place</p>
          </div>
        </div>
        <div className="row">
          {profile.role === Role.MANAGER && (
            <div className="col d-flex">
              <Link className="myHomeLink" to="/register-expert">
                <div className="card shadow-sm mb-4 text-center d-flex flex-column myHomeCard">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="50"
                    height="60"
                    fill="currentColor"
                    className="mx-auto my-4 bi bi-person-bounding-box"
                    viewBox="0 0 16 16"
                  >
                    <path d="M1.5 1a.5.5 0 0 0-.5.5v3a.5.5 0 0 1-1 0v-3A1.5 1.5 0 0 1 1.5 0h3a.5.5 0 0 1 0 1zM11 .5a.5.5 0 0 1 .5-.5h3A1.5 1.5 0 0 1 16 1.5v3a.5.5 0 0 1-1 0v-3a.5.5 0 0 0-.5-.5h-3a.5.5 0 0 1-.5-.5M.5 11a.5.5 0 0 1 .5.5v3a.5.5 0 0 0 .5.5h3a.5.5 0 0 1 0 1h-3A1.5 1.5 0 0 1 0 14.5v-3a.5.5 0 0 1 .5-.5m15 0a.5.5 0 0 1 .5.5v3a1.5 1.5 0 0 1-1.5 1.5h-3a.5.5 0 0 1 0-1h3a.5.5 0 0 0 .5-.5v-3a.5.5 0 0 1 .5-.5"/>
                    <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1zm8-9a3 3 0 1 1-6 0 3 3 0 0 1 6 0"/>
                  </svg>
                  <div className="card-body mx-4">
                    <h5 className="card-title">Create Expert</h5>
                    <p className="card-text">
                      Set up your expert profile effortlessly, making sure you
                      have credentials and expert details at hand.
                    </p>
                  </div>
                </div>
              </Link>
            </div>
          )}
          {profile.role !== Role.CUSTOMER && (
            <div className="col d-flex">
              <Link className="myHomeLink" to="/workers">
                <div className="card shadow-sm mb-4 text-center d-flex flex-column myHomeCard">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="50"
                    height="60"
                    fill="currentColor"
                    className="mx-auto my-4 bi bi-person-square"
                    viewBox="0 0 16 16"
                  >
                    <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
                    <path d="M2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2zm12 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1v-1c0-1-1-4-6-4s-6 3-6 4v1a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12z" />
                  </svg>
                  <div className="card-body mx-4">
                    <h5 className="card-title">Workers List</h5>
                    <p className="card-text">
                      Enter to view the comprehensive list containing details of
                      both managers and experts.
                    </p>
                  </div>
                </div>
              </Link>
            </div>
          )}
          {profile.role === Role.CUSTOMER && (
            <div className="col d-flex">
              <Link className="myHomeLink" to="/create-ticket">
                <div className="card shadow-sm mb-4 text-center d-flex flex-column myHomeCard">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="60"
                    height="60"
                    fill="currentColor"
                    className="mx-auto my-4 bi bi-clipboard2-plus"
                    viewBox="0 0 16 16"
                  >
                    <path d="M9.5 0a.5.5 0 0 1 .5.5.5.5 0 0 0 .5.5.5.5 0 0 1 .5.5V2a.5.5 0 0 1-.5.5h-5A.5.5 0 0 1 5 2v-.5a.5.5 0 0 1 .5-.5.5.5 0 0 0 .5-.5.5.5 0 0 1 .5-.5h3Z" />
                    <path d="M3 2.5a.5.5 0 0 1 .5-.5H4a.5.5 0 0 0 0-1h-.5A1.5 1.5 0 0 0 2 2.5v12A1.5 1.5 0 0 0 3.5 16h9a1.5 1.5 0 0 0 1.5-1.5v-12A1.5 1.5 0 0 0 12.5 1H12a.5.5 0 0 0 0 1h.5a.5.5 0 0 1 .5.5v12a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5v-12Z" />
                    <path d="M8.5 6.5a.5.5 0 0 0-1 0V8H6a.5.5 0 0 0 0 1h1.5v1.5a.5.5 0 0 0 1 0V9H10a.5.5 0 0 0 0-1H8.5V6.5Z" />
                  </svg>
                  <div className="card-body mx-4">
                    <h5 className="card-title">New Ticket</h5>
                    <p className="card-text">
                      Create your ticket with ease, ensuring you have your
                      warranty code at hand.
                    </p>
                  </div>
                </div>
              </Link>
            </div>
          )}
          <div className="col d-flex">
            <Link className="myHomeLink" to="/open-tickets">
              <div className="card shadow-sm mb-4 text-center d-flex flex-column myHomeCard">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="60"
                  height="60"
                  fill="currentColor"
                  className="mx-auto my-4 bi bi-clipboard2-heart"
                  viewBox="0 0 16 16"
                >
                  <path d="M10.058.501a.501.501 0 0 0-.5-.501h-2.98c-.276 0-.5.225-.5.501A.499.499 0 0 1 5.582 1a.497.497 0 0 0-.497.497V2a.5.5 0 0 0 .5.5h4.968a.5.5 0 0 0 .5-.5v-.503A.497.497 0 0 0 10.555 1a.499.499 0 0 1-.497-.499Z" />
                  <path d="M3.605 2a.5.5 0 0 0-.5.5v12a.5.5 0 0 0 .5.5h9a.5.5 0 0 0 .5-.5v-12a.5.5 0 0 0-.5-.5h-.5a.5.5 0 0 1 0-1h.5a1.5 1.5 0 0 1 1.5 1.5v12a1.5 1.5 0 0 1-1.5 1.5h-9a1.5 1.5 0 0 1-1.5-1.5v-12a1.5 1.5 0 0 1 1.5-1.5h.5a.5.5 0 0 1 0 1h-.5Z" />
                  <path d="M8.068 6.482c1.656-1.673 5.795 1.254 0 5.018-5.795-3.764-1.656-6.69 0-5.018Z" />
                </svg>
                <div className="card-body mx-4">
                  <h5 className="card-title">Open Tickets</h5>
                  <p className="card-text">
                    Monitor your tickets, stay informed about progress, read new
                    messages, and let us take care of the rest.
                  </p>
                </div>
              </div>
            </Link>
          </div>
          {profile.role !== Role.EXPERT && (
            <div className="col d-flex">
              <Link className="myHomeLink" to="/closed-tickets">
                <div className="card shadow-sm mb-4 text-center d-flex flex-column myHomeCard">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="60"
                    height="60"
                    fill="currentColor"
                    className="mx-auto my-4 bi bi-clipboard2-minus"
                    viewBox="0 0 16 16"
                  >
                    <path d="M9.5 0a.5.5 0 0 1 .5.5.5.5 0 0 0 .5.5.5.5 0 0 1 .5.5V2a.5.5 0 0 1-.5.5h-5A.5.5 0 0 1 5 2v-.5a.5.5 0 0 1 .5-.5.5.5 0 0 0 .5-.5.5.5 0 0 1 .5-.5h3Z" />
                    <path d="M3 2.5a.5.5 0 0 1 .5-.5H4a.5.5 0 0 0 0-1h-.5A1.5 1.5 0 0 0 2 2.5v12A1.5 1.5 0 0 0 3.5 16h9a1.5 1.5 0 0 0 1.5-1.5v-12A1.5 1.5 0 0 0 12.5 1H12a.5.5 0 0 0 0 1h.5a.5.5 0 0 1 .5.5v12a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5v-12Z" />
                    <path d="M6 8a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1H6Z" />
                  </svg>
                  <div className="card-body mx-4">
                    <h5 className="card-title">Closed Tickets</h5>
                    <p className="card-text">
                      Review past resolutions, appreciate our dedicated service,
                      and experience the peace of mind we deliver.
                    </p>
                  </div>
                </div>
              </Link>
            </div>
          )}
          {profile.role === Role.CUSTOMER && (
            <div className="col d-flex">
              <Link className="myHomeLink" to="/warranty">
                <div className="card shadow-sm mb-4 text-center d-flex flex-column myHomeCard">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="60"
                    height="60"
                    fill="currentColor"
                    className="mx-auto my-4 bi bi-shield-plus"
                    viewBox="0 0 16 16"
                  >
                    <path d="M5.338 1.59a61 61 0 0 0-2.837.856.48.48 0 0 0-.328.39c-.554 4.157.726 7.19 2.253 9.188a10.7 10.7 0 0 0 2.287 2.233c.346.244.652.42.893.533q.18.085.293.118a1 1 0 0 0 .101.025 1 1 0 0 0 .1-.025q.114-.034.294-.118c.24-.113.547-.29.893-.533a10.7 10.7 0 0 0 2.287-2.233c1.527-1.997 2.807-5.031 2.253-9.188a.48.48 0 0 0-.328-.39c-.651-.213-1.75-.56-2.837-.855C9.552 1.29 8.531 1.067 8 1.067c-.53 0-1.552.223-2.662.524zM5.072.56C6.157.265 7.31 0 8 0s1.843.265 2.928.56c1.11.3 2.229.655 2.887.87a1.54 1.54 0 0 1 1.044 1.262c.596 4.477-.787 7.795-2.465 9.99a11.8 11.8 0 0 1-2.517 2.453 7 7 0 0 1-1.048.625c-.28.132-.581.24-.829.24s-.548-.108-.829-.24a7 7 0 0 1-1.048-.625 11.8 11.8 0 0 1-2.517-2.453C1.928 10.487.545 7.169 1.141 2.692A1.54 1.54 0 0 1 2.185 1.43 63 63 0 0 1 5.072.56"/>
                    <path d="M8 4.5a.5.5 0 0 1 .5.5v1.5H10a.5.5 0 0 1 0 1H8.5V9a.5.5 0 0 1-1 0V7.5H6a.5.5 0 0 1 0-1h1.5V5a.5.5 0 0 1 .5-.5"/>
                  </svg>
                  <div className="card-body mx-4">
                    <h5 className="card-title">Add Warranties</h5>
                    <p className="card-text">
                      Connect your warranty to your account, ensuring seamless
                      access and personalized management.
                    </p>
                  </div>
                </div>
              </Link>
            </div>
          )}
        </div>
      </div>
    </>
  );
}

export default Home;
