# Expense Tracker Frontend

A React + TypeScript frontend for the expense tracker app. It uses Vite for development/builds, Redux Toolkit Query for API state, and shadcn/ui components for the interface.

## Tech Stack

- React 19
- TypeScript
- Vite
- Redux Toolkit + RTK Query
- Tailwind CSS v4
- shadcn/ui + Radix UI

## Getting Started

### Prerequisites

- Node.js 20+
- npm

### Install dependencies

```bash
npm install
```

### Start the development server

```bash
npm run dev
```

The app will usually be available at [http://localhost:5173](http://localhost:5173).

## Available Scripts

- `npm run dev` starts the Vite dev server
- `npm run build` runs TypeScript checks and creates a production build
- `npm run preview` serves the production build locally
- `npm run lint` runs ESLint

## API Configuration

The frontend currently uses a deployed backend API configured in [src/store/baseApi.ts](/Users/noelpolakallu/Desktop/expense-tracker/frontend/src/store/baseApi.ts:3):

`https://expense-tracker-production-4587.up.railway.app/api/v1`

If you want to switch to a local or different backend, update the `API_BASE_URL` constant in that file.

## Project Structure

```text
src/
  components/
    layout/       App-level layout pieces
    ui/           shadcn/ui components
  features/
    expenses/     Expense form, list, dashboard, and API hooks
    users/        User creation, switching, and API hooks
  lib/            Shared utilities
  store/          Redux store and RTK Query base API
```

## Features

- Create and switch between users
- Add expenses with amount, category, date, and description
- Filter expenses by category
- Sort expenses by newest or oldest
- View a running total for the selected user

## Styling

- UI components are built with shadcn/ui
- Design tokens come from the shared Tailwind/shadcn theme in [src/index.css](/Users/noelpolakallu/Desktop/expense-tracker/frontend/src/index.css:1)
- Shared table behavior lives in [src/components/ui/table.tsx](/Users/noelpolakallu/Desktop/expense-tracker/frontend/src/components/ui/table.tsx:1)

## Build

To create a production build:

```bash
npm run build
```

The output is generated in the `dist/` directory.
