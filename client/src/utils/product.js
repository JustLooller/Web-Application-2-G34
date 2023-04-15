export class ProductDTO {
    constructor(ean, brand, model, description, image) {
        this.ean = ean;
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.image = image;
    }

    static empty() {
        return new ProductDTO('', '', '', '', '');
    }

    static fromJson(json) {
        if (json instanceof String) {
            json = JSON.parse(json);
        }
        return new ProductDTO(json.ean, json.brand, json.model, json.description, json.image);
    }

    static async getProductDetails(ean) {
        const URL = 'http://localhost:3000/api/products/' + ean;
        try {
            const response = await fetch(URL);
            const json = await response.json();
            return Product.fromJson(json);
        } catch (error) {
            console.log(error);
        }
    }

}