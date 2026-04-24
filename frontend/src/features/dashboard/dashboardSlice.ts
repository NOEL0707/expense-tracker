import { createSlice, type PayloadAction } from "@reduxjs/toolkit";

type SortOrder = "date_desc" | "date_asc";

type DashboardState = {
  selectedUserId: string;
  category: string;
  sort: SortOrder;
  page: number;
  size: number;
};

const STORAGE_KEY = "expense-tracker-dashboard";

function getInitialState(): DashboardState {
  if (typeof window === "undefined") {
    return {
      selectedUserId: "",
      category: "all",
      sort: "date_desc",
      page: 0,
      size: 10,
    };
  }

  const raw = window.localStorage.getItem(STORAGE_KEY);

  if (!raw) {
    return {
      selectedUserId: "",
      category: "all",
      sort: "date_desc",
      page: 0,
      size: 10,
    };
  }

  try {
    return JSON.parse(raw) as DashboardState;
  } catch {
    return {
      selectedUserId: "",
      category: "all",
      sort: "date_desc",
      page: 0,
      size: 10,
    };
  }
}

const initialState = getInitialState();

const dashboardSlice = createSlice({
  name: "dashboard",
  initialState,
  reducers: {
    setSelectedUserId(state, action: PayloadAction<string>) {
      state.selectedUserId = action.payload;
    },
    setCategory(state, action: PayloadAction<string>) {
      state.category = action.payload;
      state.page = 0;
    },
    setSort(state, action: PayloadAction<SortOrder>) {
      state.sort = action.payload;
      state.page = 0;
    },
    setPage(state, action: PayloadAction<number>) {
      state.page = action.payload;
    },
    setSize(state, action: PayloadAction<number>) {
      state.size = action.payload;
      state.page = 0;
    },
    resetFilters(state) {
      state.category = "all";
      state.sort = "date_desc";
      state.page = 0;
      state.size = 10;
    },
  },
});

export const {
  setSelectedUserId,
  setCategory,
  setSort,
  setPage,
  setSize,
  resetFilters,
} = dashboardSlice.actions;

export const dashboardReducer = dashboardSlice.reducer;
export const DASHBOARD_STORAGE_KEY = STORAGE_KEY;
