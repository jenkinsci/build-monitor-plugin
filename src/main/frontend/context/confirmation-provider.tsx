import { createContext, ReactNode, useContext } from "react";

interface ProviderResponse {
  createConfirmation: (title: string, message: string, href: string) => void;
}

declare global {
  interface Window {
    dialog: any;
    crumb: any;
  }
}

const ConfirmationContext = createContext<ProviderResponse | undefined>(
  undefined,
);

export const UseConfirmationProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  function createConfirmation(title: string, message: string, href: string) {
    return window.dialog.confirm(title, { message, type: "destructive" }).then(
      () => {
        const form = document.createElement("form");
        form.setAttribute("method", "POST");
        form.setAttribute("action", href);
        window.crumb.appendToForm(form);
        document.body.appendChild(form);
        form.submit();
        return true;
      },
      () => {},
    );
  }

  return (
    <ConfirmationContext.Provider
      value={{
        createConfirmation,
      }}
    >
      {children}
    </ConfirmationContext.Provider>
  );
};

export const useConfirmation = (): ProviderResponse => {
  const context = useContext(ConfirmationContext);
  if (!context) {
    throw new Error(
      "useConfirmation must be used within a UseConfirmationProvider",
    );
  }
  return context;
};
