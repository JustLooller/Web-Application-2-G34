
export const roles = {
    "user": "Utente",
    "admin": "Amministratore"
}


export class ProfileDTO {
    constructor(name, email, age) {
        this.name = name;
        this.email = email;
        // this.role = role;
        // this.gender = gender;
        this.age = age;
    }

    static empty() {
        return new ProfileDTO("","", 0);
    }

    static fromJson(json) {
        if (json instanceof String) {
            json = JSON.parse(json);
        }
        let p = ProfileDTO.empty();
        p.name = json.name ?? "";
        p.email = json.email ?? "";
        p.age = json.age ?? 0;
        // p.role = json.role ?? "";
        // p.gender = json.gender ?? "";
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
        return await fetch(URL, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(this)
        });
    }

    async insertNewProfile() {
        const URL = "/api/profiles/";
        return await fetch(URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(this)
        });
    }
}
