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
  isResettable: boolean;
}

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
  defaults,
}: {
  monitorId: string;
  children: ReactNode;
  defaults: {
    textSize: number;
    maximumNumberOfColumns: number;
    colorBlindMode: boolean;
    showBadges: boolean;
  };
}) => {
  const colorBlindKey = makeKey(monitorId, "colorBlind");
  const textSizeKey = makeKey(monitorId, "textSize");
  const maximumNumberOfColumnsKey = makeKey(monitorId, "numberOfColumns");
  const showBadgesKey = makeKey(monitorId, "showBadges");

  const [colorBlindMode, setColorBlindModeState] = useState<boolean>(
    loadFromLocalStorage(colorBlindKey, defaults.colorBlindMode),
  );
  const [textSize, setTextSizeState] = useState<number>(
    loadFromLocalStorage(textSizeKey, defaults.textSize),
  );
  const [maximumNumberOfColumns, setMaximumNumberOfColumnsState] =
    useState<number>(
      loadFromLocalStorage(
        maximumNumberOfColumnsKey,
        defaults.maximumNumberOfColumns,
      ),
    );
  const [showBadges, setShowBadgesState] = useState<boolean>(
    loadFromLocalStorage(showBadgesKey, defaults.showBadges),
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
    setColorBlindModeState(defaults.colorBlindMode);
    setTextSizeState(defaults.textSize);
    setMaximumNumberOfColumnsState(defaults.maximumNumberOfColumns);
    setShowBadgesState(defaults.showBadges);
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
        isResettable:
          colorBlindMode === defaults.colorBlindMode &&
          textSize === defaults.textSize &&
          maximumNumberOfColumns === defaults.maximumNumberOfColumns &&
          showBadges === defaults.showBadges,
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
