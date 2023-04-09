
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
    }

    static empty() {
        return new ProfileDTO("", "", "", "", "")
    }

    static fromJson(json) {
        if (json instanceof String) {
            json = JSON.parse(json);
        }
        let p = ProfileDTO.empty();
        p.name = json.name ?? "";
        p.surname = json.surname ?? "";
        p.email = json.email ?? "";
        p.role = json.role ?? "";
        p.gender = json.gender ?? "";
        return p;
    }

    static async fetchProfile(email) {
        // const URL = "/api/profiles/" + email;
        console.log("fetchProfile: " + email);
        const URL = "https://random-data-api.com/api/v2/users"
        const response = await fetch(URL);
        if (!response.ok) {
            console.log("Error fetching profile: " + response.status)
            // throw new Error("Error fetching profile");
            return ProfileDTO.empty()
        }
        const json = await response.json();
        const profile = ProfileDTO.fromJson(json);
        console.log(json)
        profile.email = email
        return profile
    }

    async updateExistingProfile() {
        const URL = "/api/profiles/" + this.email;
        const response = await fetch(URL, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(this)
        });
        return response;
    }

    async insertNewProfile() {
        const URL = "/api/profiles/";
        const response = await fetch(URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(this)
        });
        return response;
    }
}
