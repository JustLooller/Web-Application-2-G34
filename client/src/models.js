export const Role = {
    CUSTOMER: "CUSTOMER",
    EXPERT: "EXPERT",
    MANAGER: "MANAGER",
}

export class Profile {

    /**
     *
     * @param {string} name
     * @param {string} email
     * @param {string} role
     */
    constructor(name, email, role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }

    static fromJson(json) {
        return new Profile(json.name, json.email, json.role);
    }

}

export class Token {
    constructor(access_token, expires_in, refresh_expires_in, refresh_token, token_type, not_before_policy, session_state, scope, retrieved_at) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_expires_in = refresh_expires_in;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.not_before_policy = not_before_policy;
        this.session_state = session_state;
        this.scope = scope;
        this.retrieved_at = retrieved_at ?? new Date().valueOf();
    }

    /**
     *
     * @param {object | string} json
     * @returns {Token}
     */
    static fromJson(json) {
        if (typeof json === "string")
            json = JSON.parse(json)
        return new Token(json.access_token, json.expires_in, json.refresh_expires_in, json.refresh_token, json.token_type, json['not_before_policy'], json.session_state, json.scope, json.retrieved_at);
    }

    stringify() {
        return JSON.stringify({
            access_token: this.access_token,
            expires_in: this.expires_in,
            refresh_expires_in: this.refresh_expires_in,
            refresh_token: this.refresh_token,
            token_type: this.token_type,
            "not-before-policy": this.not_before_policy,
            session_state: this.session_state,
            scope: this.scope,
            retrieved_at: this.retrieved_at
        })
    }

    isExpired() {
        return this.expires_in && (new Date().valueOf() - this.retrieved_at) > this.expires_in * 1000
    }
}

export class Product {

    /**
     *
     * @param {string} ean
     * @param {Brand} brand
     * @param {string} model
     * @param {string} description
     * @param {string} image
     */
    constructor(ean, brand, model, description, image) {
        this.ean = ean;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.image = image;
    }

    /**
     *
     * @param {object | string} json
     * @returns {Product}
     */
    static fromJson(json) {
        if (typeof json === "string")
            json = JSON.parse(json)
        return new Product(json.ean, Brand.fromJson(json.brand), json.model, json.description, json.image);
    }
}


export class Warranty {


    /**
     *
     * @param {string} id
     * @param {Product} product
     * @param {Profile} buyer
     * @param {string} warranty_start - ISO 8601
     * @param {string} warranty_end - ISO 8601
     */
    constructor(id, product, buyer, warranty_start, warranty_end) {
        this.id = id;
        this.product = product;
        this.buyer = buyer;
        this.warranty_start = warranty_start;
        this.warranty_end = warranty_end;
    }

    /**
     *
     * @param {object | string} json
     * @returns {Warranty}
     */
    static fromJson(json) {
        if (typeof json === "string")
            json = JSON.parse(json)
        return new Warranty(json.id, Product.fromJson(json.product), Profile.fromJson(json.buyer), json.warranty_start, json.warranty_end);
    }

    isExpired() {
        return new Date().valueOf() > Date.parse(this.warranty_end)
    }
}

export class Brand {

    /**
     *
     * @param {number} id
     * @param {string }name
     */
    constructor(id, name) {
        this.id = id;
        this.name = name;
    }

    /**
     *
     * @param {object | string} json
     * @returns {Brand}
     */
    static fromJson(json) {
        if (typeof json === "string")
            json = JSON.parse(json)
        return new Brand(json.id, json.name);
    }


}

export class Ticket {
    /**
     * @param {Number} id
     * @param {String} priority
     * @param {String} state
     * @param {String} creator_email
     * @param {String} expert_mail
     * @param {String} product_ean
     * @param {String} sale_id
     */
    constructor(id = null, priority = null, state = "OPEN", creator_email, expert_mail = null, product_ean, sale_id) {
        this.id = id;
        this.priority = priority;
        this.state = state;
        this.creator_email = creator_email;
        this.expert_mail = expert_mail;
        this.product_ean = product_ean;
        this.sale_id = sale_id;
    }

    /**
     *
     * @param {object | string} json
     * @returns {Ticket}
     */
    static fromJson(json) {
        if (typeof json === "string")
            json = JSON.parse(json)
        return new Ticket(json.id, json.priority, json.state, json.creator_email, json.expert_mail, json.product_ean, json.sale_id);
    }


}

export class Message {
    /**
     *
     * @param {String} text
     * @param {String} user_mail
     * @param {Number} ticket_id
     * @param {String} attachment
     */
    constructor(text, user_mail, ticket_id, attachment) {
        this.text = text;
        this.user_mail = user_mail;
        this.ticket_id = ticket_id;
        this.attachment = attachment;
    }

    /**
     *
     * @param {object | string} json
     * @returns {Message}
     */
    static fromJson(json) {
        if (typeof json === "string")
            json = JSON.parse(json)
        return new Message(json.text, json.user_mail, json.ticket_id, json.attachment);
    }

    static toJson(message){
        return JSON.stringify(message);
    }

    static fileName(attachement){
        let splitted = attachement.split('_');
        splitted.shift()
        return splitted.join("_");
    }
}

export class StateHistory {
    /**
     * StateHistoryDTO(
     *     var id: Long?,
     *     var timestamp: LocalDateTime,
     *     var status: String,
     *     var ticket_id: Long,
     *     var user_mail: String,
     * )
     */

    /**
     * @param {Number} id
     * @param {String} timestamp
     * @param {String} status
     * @param {Number} ticket_id
     * @param {String} user_mail
     */
    constructor(id, timestamp, status, ticket_id, user_mail) {
        this.id = id;
        this.timestamp = timestamp;
        this.status = status;
        this.ticket_id = ticket_id;
        this.user_mail = user_mail;
    }

    /**
     * @param {object | string} json
     * @returns {StateHistory}
     */
    static fromJson(json) {
        if (typeof json === "string")
            json = JSON.parse(json)
        return new StateHistory(json.id, json.timestamp, json.status, json.ticket_id, json.user_mail);
    }
}

// TODO: Parametrizzare azioni fattibili in base a utenza e stato ticket
export const TicketActions = {
    START: (expert_mail, priority) => {
        if (!priority) {
            priority = "LOW"
        }
        return `start/${expert_mail}?priority=${priority}`
    },
    STOP: () => "stop",
    RESOLVE: () => "resolve",
    REOPEN: () => "reopen",
    CLOSE: () => "close",
}