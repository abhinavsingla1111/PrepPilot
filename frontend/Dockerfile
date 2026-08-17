# --- Stage 1: build the React app into static files ---
FROM node:22-alpine AS build

WORKDIR /app
RUN corepack enable

# Vite reads public configuration at build time. Keep the palette selectable
# without editing source files (ocean is the blue-first default).
ARG VITE_UI_THEME=ocean
ENV VITE_UI_THEME=${VITE_UI_THEME}
ARG VITE_THEME_TRANSITION_MS=700
ENV VITE_THEME_TRANSITION_MS=${VITE_THEME_TRANSITION_MS}

# Install dependencies first (better layer caching)
COPY package.json pnpm-lock.yaml pnpm-workspace.yaml ./
# Cache the pnpm store so packages are never re-downloaded between builds.
RUN --mount=type=cache,target=/root/.local/share/pnpm/store pnpm install --frozen-lockfile

# Copy the rest of the source and build
COPY . ./
# Raise Node heap so the production bundle (incl. mermaid) doesn't OOM
ENV NODE_OPTIONS=--max-old-space-size=3072
RUN pnpm build

# --- Stage 2: serve the static files with nginx ---
FROM nginx:1.27-alpine

# Custom config: serve the SPA and proxy /api and /actuator to the backend
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80
