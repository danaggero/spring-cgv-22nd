FROM nginx:latest
RUN rm -rf /etc/nginx/conf.d/default.conf
COPY ./nginx/conf.d/default.conf /etc/nginx/conf.d/
CMD ["nginx", "-g", "daemon off;"]