type KnownApiError = {
  data?: {
    message?: string;
    error?: string;
  } | string;
  error?: string;
};

export function getErrorMessage(error: unknown, fallback: string) {
  if (typeof error === "string") {
    return error;
  }

  if (error && typeof error === "object") {
    const apiError = error as KnownApiError;

    if (typeof apiError.data === "string") {
      return apiError.data;
    }

    if (apiError.data && typeof apiError.data === "object") {
      if (apiError.data.message) {
        return apiError.data.message;
      }

      if (apiError.data.error) {
        return apiError.data.error;
      }
    }

    if (apiError.error) {
      return apiError.error;
    }
  }

  return fallback;
}
