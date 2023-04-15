/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  rewrites() {
    return [
      {
        source: "/api",
        destination: "http://localhost:8080/api/",
      },
    ];
  }
}

module.exports = nextConfig
