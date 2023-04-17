import React from "react";
import {Link} from "react-router-dom";

export default function Header() {
    return (
        <div id="header-links-container" className="flex justify-end">
            <Link to="/" id="return-to-homepage-button" name="Back to homepage"
                  className="bg-gray-100 rounded-md shadow-md px-2 py-2 mr-10 mt-5 hover:bg-gray-200 active:bg-gray-300 focus:outline-none focus:ring focus:ring-gray-400">Back
                to Homepage
            </Link>
        </div>
    )
}