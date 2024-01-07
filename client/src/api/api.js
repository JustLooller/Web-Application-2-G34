import axios from "axios";


import {Message, Product, Profile, Ticket, Token, Warranty, StateHistory} from "../models"; // eslint-disable-line no-unused-vars

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

        /**
         * Register user
         * @param username {string}
         * @param password {string}
         * @param fullName {string}
         * @param age {number}
         * @returns {Promise<boolean>}
         */
        static async register(username, password, fullName, age) {
            const body = {
                email: username,
                password,
                fullName,
                age
            };
            const response = await axios.post(API.url + '/api/signup', body);
            return response.status === 200;
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
                `${API.url}/api/profiles/${email}`
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

        /**
         *
         * @param {Ticket} ticket
         * @returns
         */
        static async ticketCreation(ticket) {
            try {
                const response = await axios.post(
                    `${API.url}/api/ticket/`,
                    ticket
                )
                return Ticket.fromJson(response.data)//Warranty.fromJson(response.data)
            } catch (e) {
                handleError("Error on ticketCreation", e)
            }
        }

        static async getTicketById(id) {
            try {
                const response = await axios.get(
                    `${API.url}/api/ticket/${id}`
                )
                return Ticket.fromJson(response.data)//Warranty.fromJson(response.data)
            } catch (e) {
                handleError("Error on getTicketById", e)
            }
        }

        /**
         *
         * @returns {Promise<Ticket[]>}
         */
        static async getTickets() {
            try {
                const response = await axios.get(
                    `${API.url}/api/tickets`
                )
                return response.data.map((it) => Ticket.fromJson(it))//Warranty.fromJson(response.data)
            } catch (e) {
                handleError("Error on getTickets", e)
            }
        }

        /**
         *
         * @param id {Number} Ticket ID
         * @return {Promise<StateHistory[]>} State History list
         */
        static async getTicketHistory(id) {
            try {
                const response = await axios.get(
                    `${API.url}/api/ticket/${id}/history`
                )
                return response.data.map((it) => StateHistory.fromJson(it))//Warranty.fromJson(response.data)
            } catch (e) {
                handleError("Error on getTicketHistory", e)
            }
        }

        /**
         * Action on ticket
         * @param id {Number} ticket id
         * @param action {string} action to perform (use TicketActions)
         * @return {Promise<boolean>}
         */
        static async action(id, action) {
            try {
                const response = await axios.put(
                    `${API.url}/api/ticket/${id}/${action}`
                )
                return response.status === 200
            }   catch (e) {
                handleError(`Error on action ${action}`, e)
            }
        }

    }

    static MessageAPI = class {

        /**
         *
         * @param {Message} message
         * @returns
         */
        static async sendMessage(message, attachment){
            try {
                var form = new FormData();
                //form.append("document", fileInput.files[0], "/C:/Users/Giacomo/Desktop/stock_dog.jpeg");
                form.append("messageDTO", new Blob([Message.toJson(message)], {type: "application/json"}));
                if(attachment !== undefined){
                    form.append("document", attachment)
                }
                const response = await axios.post(
                    `${API.url}/api/ticket/${message.ticket_id}/message`,
                    form
                )
                return //Warranty.fromJson(response.data)
            } catch (e) {
                handleError("Error on sendMessage", e)
            }
        }

        /**
         *
         * @param {String} ticketID
         * @returns {Promise<[Message]>}
         */
        static async getMessages(ticketID) {
            try {
                const response = await axios.get(
                    `${API.url}/api/ticket/${ticketID}/messages`
                )
                return response.data.map((m) => Message.fromJson(m))
            } catch (e) {
                handleError("Error on getMessages", e)
            }
        }

        static async getAttachment(ticketID, attachment){
            try{
                const response = await axios.get(
                    `${API.url}/api/ticket/${ticketID}/message/${attachment}`,
                    { responseType: "blob" }
                )
                return response.data
            }
            catch (e) {
                handleError("Error on getAttachment", e)
            }
        }


    }

    // TODO: Inserire tutti i vari endpoint che sono dentro TicketController, necesssariamente dichiarare dentro model.js anche i vari model restituiti
}