// fetch wrapper extracting json

import axios from "axios";
import {Profile} from "../hooks/auth";


export default class API {
    static url = 'http://localhost:8081'

    static Security = class {
        /**
         * To be used only in AuthProvider
         *
         * @param {string} username
         * @param {string} password
         * @returns {Promise<string|null>} JWT Token
         * @throws {Error} whenever response status != 200
         */
        static async login(username, password) {
            const body = {
                username,
                password
            }
            const response = await axios.post(API.url + '/api/login', body);
            if (response.status === 200) {
                console.log("login response", response.data)
                return response.data.access_token;
            }
            throw new Error("Login failed")

        }
    }

    static ProfileAPI = class {

        /**
         *
         * @param {string} email
         * @returns {Promise<Profile>}
         */
        static async get(email) {
            const response = await axios.get(
                `${API.url}/profiles/${email}`
            )
            if (response.status === 200) {
                return Profile.fromJson(response.data)
            }
            console.error(response)
            throw new Error("Couldn't get profile")
        }
    }

}