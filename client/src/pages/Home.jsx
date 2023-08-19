import {useAuth} from "../hooks/auth";

export default function Home() {
    const {profile, login} = useAuth();

    return <div>
        Homepage
        {profile?.name}
        <button onClick={() => login("customeruser@gmail.com", "customer")}>LOGINNN</button>
    </div>
}