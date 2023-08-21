import axios from "axios";


import {Product, Profile, Token, Warranty} from "../models"; // eslint-disable-line no-unused-vars

/**
 *
 * @param {string} msg
 * @param {AxiosError} e
 */
const handleError = (msg, e) => {
    if (e.response) {
        console.error(msg, "Error on API call", e.response)
        throw e.response
    }
    if (e.request) {
        console.error(msg, "Error on API call", e.request)
        throw e.request
    }
    console.error(msg, "Error on API call", e.message)
    throw e.message
}
export default class API {
    static url = 'http://localhost:8081'

    /**
     * To be used only in AuthProvider
     * @type {*}
     */
    static Security = class {
        /**
         * To be used only in AuthProvider
         *
         * @param {string} username
         * @param {string} password
         * @returns {Promise<Token>} JWT Token
         * @throws {Error} whenever response status != 200
         */
        static async login(username, password) {
            const body = {
                username,
                password
            }
            // empty authentication header

            const response = await axios.post(API.url + '/api/login', body, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': ''
                }
            });
            if (response.status === 200) {
                console.log("login response", response.data)
                return Token.fromJson(response.data);
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

    static ProductsAPI = class {
        /**
         *
         * @returns {Promise<Product[]>}
         * @throws {Error} whenever response status != 200
         */
        static async getAll() {
            try {
                const response = await axios.get(
                    `${API.url}/api/products/`
                )
                return Array.from(response.data).map(Product.fromJson)
            } catch (e) {
                handleError("Couldn't get products", e)
            }
        }

        /**
         *
         * @param {string} ean
         * @returns {Promise<Product>}
         * @throws {Error} whenever response status != 200
         */
        static async get(ean) {
            try {
                const response = await axios.get(
                    `${API.url}/api/products/${ean}`
                )
                return Product.fromJson(response.data)
            } catch (e) {
                handleError("Couldn't get product with ean: " + ean, e)
            }
        }
    }

    static WarrantyAPI = class {

        /**
         * Retrieves all warranties of the logged-in user
         * @returns {Promise<Warranty[]>}
         */
        static async getWarranties() {
            try {
                const response = await axios.get(`${API.url}/api/sale`)
                return Array.from(response.data).map(Warranty.fromJson)
            } catch (e) {
                handleError("Couldn't get warranties", e)
            }
        }

        /**
         * Associates a sale to the logged-in user
         * @param {string} saleId
         * @returns {Promise<Warranty>}
         */
        static async associateSale(saleId) {
            try {
                const response = await axios.post(
                    `${API.url}/api/sale/${saleId}`
                )
                return Warranty.fromJson(response.data)
            } catch (e) {
                handleError("Error on associateSale", e)
            }
        }
    }

    static TicketAPI = class {
        // TODO: Inserire tutti i vari endpoint che sono dentro TicketController, necesssariamente dichiarare dentro model.js anche i vari model restituiti
    }
}