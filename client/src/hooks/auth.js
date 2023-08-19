import API from "../api/api";
import React, {useContext, useEffect, useState} from "react";
import axios from "axios";
import jwt_decode from "jwt-decode";

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

const AuthContext = React.createContext()

export const AuthProvider = ({children}) => {
    const [token, setToken] = useState(localStorage.getItem("token"));
    /** Profile */
    const [profile, setProfile] = useState(Profile.fromJson(JSON.parse(localStorage.getItem("profile")|| "{}")));

    useEffect(() => {
        if (token) {
            axios.defaults.headers.common["Authorization"] = "Bearer " + token;
            localStorage.setItem('token', token);
            const decoded = jwt_decode(token);
            const decoded_profile = new Profile(decoded.name, decoded.email);
            const decoded_roles = Array.from(decoded.realm_access.roles)
            if (decoded_roles.includes(Role.CUSTOMER))
                decoded_profile.role = Role.CUSTOMER;
            if (decoded_roles.includes(Role.EXPERT))
                decoded_profile.role = Role.EXPERT
            if (decoded_roles.includes(Role.MANAGER))
                decoded_profile.role = Role.MANAGER
            localStorage.setItem("profile", JSON.stringify(decoded_profile))
            setProfile(decoded_profile)

        } else {
            delete axios.defaults.headers.common["Authorization"];
            setProfile(null)
            localStorage.removeItem('token')
            localStorage.removeItem('profile')
        }
    }, [token]);


    const login = (email, password) => {
        API.Security.login(email, password).then((token_response) => {
            setToken(token_response)
        })
    }

    const logout = () => {
        setToken(null)
        setProfile(null)
    }

    return (<AuthContext.Provider value={{profile, login, logout}}>
        {children}
    </AuthContext.Provider>)
}

export function useAuth() {
    const {profile, login, logout} = useContext(AuthContext)
    return {profile, login, logout}
}

