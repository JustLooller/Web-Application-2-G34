
export const roles = {
    "user": "Utente",
    "admin": "Amministratore"
}


export class ProfileDTO {
    constructor(name, surname, email, role, gender) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.role = role;
        this.gender = gender;
        this.id = email;
    }

    static empty() {
        return new ProfileDTO("", "", "", "", "")
    }

    static fromJson(json) {
        let p = ProfileDTO.empty();
        p.name = json.name ?? "";
        p.surname = json.surname ?? "";
        p.email = json.email ?? "";
        p.role = json.role ?? "";
        p.gender = json.gender ?? "";
        p.id = json.id ?? p.email;
        return p;
    }

    static async fetchProfile(email) {
        // const URL = "http://localhost:3000/api/profiles/" + email;
        const URL = "https://random-data-api.com/api/v2/users"
        const profile = ProfileDTO.fromJson(await (await fetch(URL)).json());
        profile.email = email
        return profile
    }
}
