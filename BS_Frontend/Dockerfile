FROM node:18
WORKDIR /app

COPY package*.json ./

RUN npm install --legacy-peer-deps

COPY . .

EXPOSE 8888

CMD ["npm", "run", "dev"]
