import API from "../api/api";
import React, {useContext, useEffect, useState} from "react";
import axios from "axios";
import jwt_decode from "jwt-decode";

import {Profile, Token, Role} from '../models'

const AuthContext = React.createContext()

export const AuthProvider = ({children}) => {
    const [token, setToken] = useState(Token.fromJson(localStorage.getItem("token") || "{}"));
    /** Profile */
    const [profile, setProfile] = useState(Profile.fromJson(JSON.parse(localStorage.getItem("profile") || "{}")));

    useEffect(() => {
        const succesfulLoggedIn = token !== undefined && token !== null && token.isExpired() === false;

        if (succesfulLoggedIn) {
            axios.defaults.headers.common["Authorization"] = "Bearer " + token.access_token;
            localStorage.setItem('token', token.stringify());
            const decoded = jwt_decode(token.access_token);
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


    /**
     *
     * @param {string} email
     * @param {string} password
     * @returns {Promise<boolean>}
     */
    const login = async (email, password) => {
        try {
            const token_response = await API.Security.login(email, password)
            setToken(token_response);
        } catch (e) {
            console.error("Error on login", e)
            return false;
        }
        return true;
    }

    const logout = () => {
        setToken(null)
        setProfile(null)
    }

    return (<AuthContext.Provider value={{profile, login, logout}}>
        {children}
    </AuthContext.Provider>)
}

/**
 *
 * @returns {{logout: () => void, profile: Profile, login: (username, password) => Promise<boolean>}}
 */
export function useAuth() {

    const {profile, login, logout} = useContext(AuthContext)
    return {profile, login, logout}
}

