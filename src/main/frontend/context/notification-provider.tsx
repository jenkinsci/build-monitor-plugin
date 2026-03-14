import { createContext, ReactNode, useContext } from "react";

interface ProviderResponse {
  createNotification: (error: string) => void;
}

declare global {
  interface Window {
    notificationBar: any;
  }
}

const NotificationContext = createContext<ProviderResponse | undefined>(
  undefined,
);

export const UseNotificationProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  function createNotification(error: string) {
    window.notificationBar.show(error, window.notificationBar.ERROR);
  }

  return (
    <NotificationContext.Provider
      value={{
        createNotification,
      }}
    >
      {children}
    </NotificationContext.Provider>
  );
};

export const useNotification = (): ProviderResponse => {
  const context = useContext(NotificationContext);
  if (!context) {
    throw new Error(
      "useNotification must be used within a UseNotificationProvider",
    );
  }
  return context;
};
