FROM azul/zulu-openjdk-alpine:17.0.6

RUN adduser -s /bin/true -u 1000 -D -h /app-home app-user \
  && chown -R app-user /app-home \
  && chmod 700 /app-home

WORKDIR /app-home

USER app-user

ADD ./app-bundle.tar ./
CMD ./app-bundle/bin/app