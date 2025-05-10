import Tippy, { TippyProps } from "@tippyjs/react";
import { isValidElement, ReactElement, ReactNode, useState } from "react";

import Tooltip from "./tooltip.tsx";

/**
 * A customized (and customizable) implementation of Tippy dropdowns
 */
export default function Dropdown({
  items,
  disabled,
  className,
}: DropdownProps) {
  const [visible, setVisible] = useState(false);
  const show = () => setVisible(true);
  const hide = () => setVisible(false);

  return (
    <Tooltip content={"More actions"}>
      <Tippy
        visible={visible}
        onClickOutside={hide}
        {...DefaultDropdownProps}
        content={
          <div className="jenkins-dropdown">
            {items.map((item, index) => {
              if (item === "separator") {
                return (
                  <div
                    key={`separator-${index}`}
                    className="jenkins-dropdown__separator"
                  />
                );
              }

              if (isValidElement(item)) {
                return (
                  <div key={index} className="jenkins-dropdown__custom-item">
                    {item}
                  </div>
                );
              }

              const dropdownItem = item as DropdownItem;
              return (
                <a
                  key={index}
                  className="jenkins-dropdown__item"
                  href={dropdownItem.href}
                  target={dropdownItem.target}
                  download={dropdownItem.download}
                >
                  <div className="jenkins-dropdown__item__icon">
                    {dropdownItem.icon}
                  </div>
                  {dropdownItem.text}
                </a>
              );
            })}
          </div>
        }
      >
        <button
          className={"jenkins-button " + className}
          type="button"
          disabled={disabled}
          onClick={visible ? hide : show}
        >
          <div className="jenkins-overflow-button__ellipsis">
            <span />
            <span />
            <span />
          </div>
        </button>
      </Tippy>
    </Tooltip>
  );
}

export const DefaultDropdownProps: TippyProps = {
  theme: "dropdown",
  duration: 250,
  touch: true,
  animation: "dropdown",
  interactive: true,
  offset: [0, 0],
  placement: "bottom-start",
  arrow: false,
};

interface DropdownProps {
  items: (DropdownItem | ReactElement | "separator")[];
  disabled?: boolean;
  className?: string;
}

interface DropdownItem {
  text: string;
  href?: string;
  icon: ReactNode;
  target?: string;
  download?: string;
}
