import moment from "moment";

export function formatDateFromArray(dateArray, options = {}) {
  if (!Array.isArray(dateArray) || dateArray.length < 3) return "";

  const [year, month, day, hour = 0, minute = 0, second = 0] = dateArray;
  const date = new Date(year, month - 1, day, hour, minute, second);

  const { locale = "vi-VN", includeTime = false } = options;

  const dateOptions = {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    ...(includeTime && {
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
    }),
  };

  return date.toLocaleString(locale, dateOptions);
}

export const formatDate = (dateArray) => {
  if (Array.isArray(dateArray)) {
    const [year, month, ...rest] = dateArray;
    const date = new Date(year, month - 1, ...rest);
    return moment(date).fromNow();
  }

  if (typeof dateArray === "string") {
    return moment(dateArray).fromNow();
  }
};
