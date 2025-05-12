import { createContext, ReactNode, useContext } from "react";

interface ProviderResponse {
  createDialog: (error: string) => void;
}

const DialogContext = createContext<ProviderResponse | undefined>(undefined);

export const UseDialogProvider = ({ children }: { children: ReactNode }) => {
  function createDialog(error: string) {
    alert(error);
  }

  return (
    <DialogContext.Provider
      value={{
        createDialog,
      }}
    >
      {children}
    </DialogContext.Provider>
  );
};

export const useDialog = (): ProviderResponse => {
  const context = useContext(DialogContext);
  if (!context) {
    throw new Error("useDialog must be used within a UseDialogProvider");
  }
  return context;
};
