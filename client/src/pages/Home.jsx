import {useAuth} from "../hooks/auth";
import API from "../api/api";

export default function Home() {
    const {profile, login} = useAuth();

    return <div>
        Homepage
        {profile?.name}
        <button onClick={() => login("customeruser@gmail.com", "customer")}>LOGINNN</button>
        <button onClick={() => API.ProfileAPI.get("customeruser@gmail.com")}> TEST API </button>
    </div>
}