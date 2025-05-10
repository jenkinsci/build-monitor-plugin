import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from "react";

interface MonitorPreferences {
  colorBlindMode: boolean;
  textSize: number;
  maximumNumberOfColumns: number;
  showBadges: boolean;
  setColorBlindMode: (val: boolean) => void;
  setTextSize: (val: number) => void;
  setMaximumNumberOfColumns: (val: number) => void;
  setShowBadges: (val: boolean) => void;
  reset: () => void;
}

const defaultPreferences = {
  colorBlindMode: false,
  textSize: 1,
  maximumNumberOfColumns: 3,
  showBadges: true,
};

const UserPreferencesContext = createContext<MonitorPreferences | undefined>(
  undefined,
);

const makeKey = (monitorId: string, setTing: string) =>
  `buildMonitor.${monitorId}.${setTing}`;

const loadFromLocalStorage = <T,>(key: string, fallback: T): T => {
  if (typeof window === "undefined") return fallback;
  try {
    const value = window.localStorage.getItem(key);
    if (value !== null) {
      if (typeof fallback === "boolean") {
        return (value === "true") as typeof fallback;
      }
      if (typeof fallback === "number") {
        return Number(value) as T;
      }
      return value as unknown as T;
    }
  } catch (e) {
    console.error(`Error loading localStorage key "${key}"`, e);
  }
  return fallback;
};

export const UserPreferencesProvider = ({
  monitorId,
  children,
}: {
  monitorId: string;
  children: ReactNode;
}) => {
  const colorBlindKey = makeKey(monitorId, "colorBlind");
  const textSizeKey = makeKey(monitorId, "textSize");
  const maximumNumberOfColumnsKey = makeKey(monitorId, "numberOfColumns");
  const showBadgesKey = makeKey(monitorId, "showBadges");

  const [colorBlindMode, setColorBlindModeState] = useState<boolean>(
    loadFromLocalStorage(colorBlindKey, defaultPreferences.colorBlindMode),
  );
  const [textSize, setTextSizeState] = useState<number>(
    loadFromLocalStorage(textSizeKey, defaultPreferences.textSize),
  );
  const [maximumNumberOfColumns, setMaximumNumberOfColumnsState] =
    useState<number>(
      loadFromLocalStorage(
        maximumNumberOfColumnsKey,
        defaultPreferences.maximumNumberOfColumns,
      ),
    );
  const [showBadges, setShowBadgesState] = useState<boolean>(
    loadFromLocalStorage(showBadgesKey, defaultPreferences.showBadges),
  );

  useEffect(() => {
    window.localStorage.setItem(colorBlindKey, String(colorBlindMode));
  }, [colorBlindMode]);

  useEffect(() => {
    window.localStorage.setItem(textSizeKey, String(textSize));
  }, [textSize]);

  useEffect(() => {
    window.localStorage.setItem(
      maximumNumberOfColumnsKey,
      String(maximumNumberOfColumns),
    );
  }, [maximumNumberOfColumns]);

  useEffect(() => {
    window.localStorage.setItem(showBadgesKey, String(showBadges));
  }, [showBadges]);

  function reset() {
    setColorBlindModeState(defaultPreferences.colorBlindMode);
    setTextSizeState(defaultPreferences.textSize);
    setMaximumNumberOfColumnsState(defaultPreferences.maximumNumberOfColumns);
    setShowBadgesState(defaultPreferences.showBadges);

    window.localStorage.setItem(
      colorBlindKey,
      String(defaultPreferences.colorBlindMode),
    );
    window.localStorage.setItem(
      textSizeKey,
      String(defaultPreferences.textSize),
    );
    window.localStorage.setItem(
      maximumNumberOfColumnsKey,
      String(defaultPreferences.maximumNumberOfColumns),
    );
    window.localStorage.setItem(
      showBadgesKey,
      String(defaultPreferences.showBadges),
    );
  }

  return (
    <UserPreferencesContext.Provider
      value={{
        colorBlindMode,
        textSize,
        maximumNumberOfColumns,
        showBadges,
        setColorBlindMode: setColorBlindModeState,
        setTextSize: setTextSizeState,
        setMaximumNumberOfColumns: setMaximumNumberOfColumnsState,
        setShowBadges: setShowBadgesState,
        reset,
      }}
    >
      {children}
    </UserPreferencesContext.Provider>
  );
};

export const useUserPreferences = (): MonitorPreferences => {
  const context = useContext(UserPreferencesContext);
  if (!context) {
    throw new Error(
      "useMonitorPreferences must be used within a MonitorPreferencesProvider",
    );
  }
  return context;
};
